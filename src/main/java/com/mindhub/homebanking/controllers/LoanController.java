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

            if (loanApplicationDTO.getId() <= 0){
                return new ResponseEntity<>("el id es incorrecto", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getName().isBlank()){
                return new ResponseEntity<>("el nombre esta vacio", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getAmount() <=0){
                return new ResponseEntity<>("el monto es incorrecto", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getPayments() <=0){
                return new ResponseEntity<>("las cuotas son incorrectas", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getNumber().isBlank()){
                return new ResponseEntity<>("el numero esta vacio", HttpStatus.FORBIDDEN);
            }


            Loan loan = loanService.findById(loanApplicationDTO.getId());

            Client client = clientService.findByEmail(authentication.getName());

            Account account = accountService.findByNumberAndClient(loanApplicationDTO.getNumber(), client);


            if (loan == null){
                return new ResponseEntity<>("no existe el loan", HttpStatus.UNAUTHORIZED);
            }

            if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
                return new ResponseEntity<>("esta mal el monto ingresado", HttpStatus.FORBIDDEN);
            }

            if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
                return new ResponseEntity<>("cuotas incorrectas", HttpStatus.FORBIDDEN);
            }

            if (account == null) {

                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

            }

            double sumaAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.20);

            /*hacer la verificacion para saber si el usuario ya tiene un prestamo*/
            if (clientLoanService.existsByClientAndLoan(client,loan)){
                return new ResponseEntity<>("ya tiene este prestamo", HttpStatus.FORBIDDEN);
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
