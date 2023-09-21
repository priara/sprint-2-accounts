package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientLoanDTO;
import com.mindhub.homebanking.dto.LoanApplicationDTO;
import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {

        List<LoanDTO> loans = loanService.getLoans();

        return loans;
    }


    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> createLoans(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (loanApplicationDTO.getName().isBlank()) {
            return new ResponseEntity<>("the name is empty", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("the amount is wrong", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("the odds are wrong", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getNumber().isBlank()) {
            return new ResponseEntity<>("the number is empty", HttpStatus.FORBIDDEN);
        }


        Loan loan = loanService.findByName(loanApplicationDTO.getName());

        Client client = clientService.findByEmail(authentication.getName());

        Account account = accountService.findByNumberAndClient(loanApplicationDTO.getNumber(), client);


        if (loan == null) {
            return new ResponseEntity<>("the loan does not exist", HttpStatus.UNAUTHORIZED);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("the amount entered is wrong", HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("wrong odds", HttpStatus.FORBIDDEN);
        }

        if (account == null) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        double sumaAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * (loan.getPercentage() / 100.0));

        /*hacer la verificacion para saber si el usuario ya tiene un prestamo*/
        if (clientLoanService.existsByClientAndLoan(client, loan)) {
            return new ResponseEntity<>("already have this loan", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(sumaAmount, loanApplicationDTO.getPayments(),loanApplicationDTO.getPayments(), true, sumaAmount );

        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanService.clientLoanSave(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), LocalDateTime.now(), loan.getName() + " loan approved", account.getBalance() + loanApplicationDTO.getAmount(), true);
        account.addTransaction(transaction);
        transactionService.transactionSave(transaction);

        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountService.addAccount(account);

        clientService.saveClient(client);
        loanService.saveLoan(loan);


        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("/api/loans/create")
    public ResponseEntity<Object> createLoanAdmin(
    @RequestBody LoanDTO loanDTO) {

        if (loanDTO.getName().isBlank()) {
            return new ResponseEntity<>("Missing name", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount() <= 0) {
            return new ResponseEntity<>("Missing max amount", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayments().isEmpty()) {
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPercentage() <= 0) {
            return new ResponseEntity<>("Missing percentage", HttpStatus.FORBIDDEN);
        }
        if (loanService.existsByName(loanDTO.getName())) {
            return new ResponseEntity<>("A loan with the same name already exists", HttpStatus.BAD_REQUEST);
        }


        Loan newLoan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(), loanDTO.getPercentage());
        loanService.saveLoan(newLoan);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @Transactional
    @PatchMapping("/api/loans")
    public ResponseEntity<Object> payLoan(@RequestParam Long id, @RequestParam String accountNumber, Authentication authentication) {

        if (accountNumber.isBlank()) {
            return new ResponseEntity<>("You must specify an account number.", HttpStatus.FORBIDDEN);
        }


        ClientLoan clientLoan = clientLoanRepository.getById(id);

        if (clientLoan == null) {
            return new ResponseEntity<>("This loan doesn't exist.", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.findByNumber(accountNumber);

        if (account == null) {
            return new ResponseEntity<>("The account doesn't exist.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() <= 0) {
            return new ResponseEntity<>("The account doesn't have enough funds.", HttpStatus.FORBIDDEN);
        }
        if (!account.getActive()) {
            return new ResponseEntity<>("The account isn't active.", HttpStatus.FORBIDDEN);
        }


        Client authClient = clientService.findByEmail(authentication.getName());

        if(!(accountService.existsByIdAndClient_Id(account.getId(), authClient.getId()))) {
            return new ResponseEntity<>("The account doesn't belong to the current user.", HttpStatus.FORBIDDEN);
        }

        double amountToPay = clientLoan.getAmount() / clientLoan.getPaymentsLeft();

        if (amountToPay <= 0 || clientLoan.getPaymentsLeft() <= 0) {
            return new ResponseEntity<>("There are no more installments to pay.", HttpStatus.FORBIDDEN);
        }
        if (amountToPay > account.getBalance()) {
            return new ResponseEntity<>("You don't have enough funds to pay this installment.", HttpStatus.FORBIDDEN);
        }


        clientLoan.setPaymentsLeft(clientLoan.getPaymentsLeft() - 1);
        if (clientLoan.getPaymentsLeft() == 0) {
            clientLoan.setActive(false);
        }
        clientLoan.setRemainingAmount(clientLoan.getAmount() - amountToPay);
        clientLoanService.clientLoanSave(clientLoan);

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amountToPay, LocalDateTime.now(), clientLoan.getLoan().getName() + " | Installment payment", account.getBalance() - amountToPay, true);
        account.addTransaction(debitTransaction);
        transactionService.transactionSave(debitTransaction);

        account.setBalance(account.getBalance() - amountToPay);
        accountService.addAccount(account);

        return new ResponseEntity<>("The installment has been paid successfully.", HttpStatus.CREATED);
    }




}
