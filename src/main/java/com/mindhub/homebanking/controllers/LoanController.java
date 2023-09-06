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

    @RequestMapping(path = "/api/loans", method = RequestMethod.GET)
    public List<LoanDTO> getLoans(){

        List<LoanDTO> loans = loanService.getLoans();

        return loans;
    }


    @Transactional
    @RequestMapping(path = "/api/loans", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransactions(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

            if (authentication == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            if (loanApplicationDTO.getName().isBlank()){
                return new ResponseEntity<>("the name is empty", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getAmount() <=0){
                return new ResponseEntity<>("the amount is wrong", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getPayments() <=0){
                return new ResponseEntity<>("the odds are wrong", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getNumber().isBlank()){
                return new ResponseEntity<>("the number is empty", HttpStatus.FORBIDDEN);
            }


            Loan loan = loanService.findByName(loanApplicationDTO.getName());

            Client client = clientService.findByEmail(authentication.getName());

            Account account = accountService.findByNumberAndClient(loanApplicationDTO.getNumber(), client);


            if (loan == null){
                return new ResponseEntity<>("the loan does not exist", HttpStatus.UNAUTHORIZED);
            }

            if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
                return new ResponseEntity<>("the amount entered is wrong", HttpStatus.FORBIDDEN);
            }

            if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
                return new ResponseEntity<>("wrong odds", HttpStatus.FORBIDDEN);
            }

            if (account == null) {

                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

            }

            double sumaAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.20);

            /*hacer la verificacion para saber si el usuario ya tiene un prestamo*/
            if (clientLoanService.existsByClientAndLoan(client,loan)){
                return new ResponseEntity<>("already have this loan", HttpStatus.FORBIDDEN);
            }

            ClientLoan clientLoan = new ClientLoan(sumaAmount, loanApplicationDTO.getPayments());

            loan.addClientLoan(clientLoan);
            client.addClientLoan(clientLoan);
            clientLoanService.clientLoanSave(clientLoan);

            Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), LocalDateTime.now(), loan.getName() + " loan approved" );
            account.addTransaction(transaction);
            transactionService.transactionSave(transaction);

            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.addAccount(account);

            clientService.saveClient(client);
            loanService.saveLoan(loan);


        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
}
