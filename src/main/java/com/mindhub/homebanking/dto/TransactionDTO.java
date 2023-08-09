package com.mindhub.homebanking.dto;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private TransactionType type;
    private double amount;
    private LocalDateTime date;

    private String description;
    public TransactionDTO() {
    }


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
    }



    public Long getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }


    public double getAmount() {
        return amount;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
