package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String cardHolder;

    private CardType type;

    private CardColor color;

    private String number;

    private int cvv;

    private String formattedDate;

    /*private LocalDate thruDate;*/

    private String formattedFutureDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card() {
    }

    public Card(String cardHolder, CardType type, CardColor color, String number, int cvv, String formattedDate, String formattedFutureDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.formattedDate = formattedDate;
        this.formattedFutureDate = formattedFutureDate;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardholder() {
        return cardHolder;
    }

    public void setCardholder(String cardholder) {
        this.cardHolder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }


    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getFormattedFutureDate() {
        return formattedFutureDate;
    }

    public void setFormattedFutureDate(String formattedFutureDate) {
        this.formattedFutureDate = formattedFutureDate;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

}
