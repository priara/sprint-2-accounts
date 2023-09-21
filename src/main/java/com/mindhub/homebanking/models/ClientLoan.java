package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double amount;

    private int payments;

    private int paymentsLeft;

    private double remainingAmount;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan(double amount, int payment, int paymentsLeft, Boolean active, double remainingAmount) {
        this.amount = amount;
        this.payments = payment;
        this.paymentsLeft= paymentsLeft;
        this.active = active;
        this.remainingAmount=remainingAmount;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payment) {
        this.payments = payment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public int getPaymentsLeft() {
        return paymentsLeft;
    }

    public void setPaymentsLeft(int paymentsLeft) {
        this.paymentsLeft = paymentsLeft;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
