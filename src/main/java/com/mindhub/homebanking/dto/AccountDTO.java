package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class AccountDTO {
    private long id;

    private String number;

    private LocalDate creationDate;

    private double balance;

    private List<TransactionDTO> transaction;
   /* hola*/
    public AccountDTO() {
    }
    public AccountDTO(Account account){

        this.number = account.getNumber();

        this.creationDate = account.getCreationDate();

        this.balance = account.getBalance();

        this.id = account.getId();

        this.transaction = account.getTransactions().stream().map(TransactionDTO::new).collect(toList());
    }


    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<TransactionDTO> getTransaction() {
        return transaction;
    }
}
