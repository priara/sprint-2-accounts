package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {

    private long id;

    private String name;

    private double amount;

    private int payments;

    private String number;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long id, double amount, int payments, String number, String name) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.number = number;
        this.name= name;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
