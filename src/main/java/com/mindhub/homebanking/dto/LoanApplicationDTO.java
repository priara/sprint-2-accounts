package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {



    private String name;

    private double amount;

    private int payments;

    private String number;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO( double amount, int payments, String number, String name) {
        this.amount = amount;
        this.payments = payments;
        this.number = number;
        this.name= name;
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
