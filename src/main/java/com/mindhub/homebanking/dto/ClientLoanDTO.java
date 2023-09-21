package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private long loan;

    private long id;

    private String name;

    private double amount;

    private int payments;


    private Boolean active;

    private int paymentsLeft;

    private double remainingAmount;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.name = clientLoan.getLoan().getName();
        this.loan = clientLoan.getLoan().getId();
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.paymentsLeft = clientLoan.getPaymentsLeft();
        this.active = clientLoan.getActive();
        this.remainingAmount=clientLoan.getRemainingAmount();
    }


    public long getLoan() {
        return loan;
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Boolean getActive() {
        return active;
    }

    public int getPaymentsLeft() {
        return paymentsLeft;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }
}
