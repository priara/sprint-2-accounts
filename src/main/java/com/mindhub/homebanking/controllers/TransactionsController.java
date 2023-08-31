package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TransactionsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/api/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransactions(

            @RequestParam double amount, @RequestParam String description,

            @RequestParam String numberOrigin, @RequestParam String numberDestiny, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (amount <= 0) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (description.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (numberOrigin.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }if (numberDestiny.isBlank()){

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


        if (numberOrigin.equals(numberDestiny)) {
            return new ResponseEntity<>("They are the same accounts, they cannot be the same ", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        Account account = accountRepository.findByNumberAndClient(numberOrigin, client);

        Account accountDest = accountRepository.findByNumber(numberDestiny);

        if ( account == null ){
            return new ResponseEntity<>("the account does not belong to the customer", HttpStatus.FORBIDDEN);
        }

        if (accountDest == null){
            return new ResponseEntity<>("destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() >= amount) {

            account.setBalance(account.getBalance() - amount);
            accountDest.setBalance(accountDest.getBalance() + amount);

            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount, LocalDateTime.now(), description + " " + account.getNumber());

            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, LocalDateTime.now(), description + " " + accountDest.getNumber());

            transactionRepository.save(transactionDebit);
            transactionRepository.save(transactionCredit);
            account.addTransaction(transactionDebit);
            accountDest.addTransaction(transactionCredit);
            accountRepository.save(account);
            accountRepository.save(accountDest);
        } else {

            return new ResponseEntity<>("there is not enough balance", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("the transfer was created", HttpStatus.CREATED);
    }
}
