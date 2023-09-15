package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardholder;

    private CardType type;

    private CardColor color;

    private String number;

    private int cvv;

    private String formattedDate;

    private String formattedFutureDate;

    private Boolean isActive;

    public CardDTO() {
    }


    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getCardholder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.formattedDate = card.getFormattedDate();
        this.formattedFutureDate = card.getFormattedFutureDate();
        this.isActive = card.getisActive();

    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }


    public String getFormattedDate() {
        return formattedDate;
    }

    public String getFormattedFutureDate() {
        return formattedFutureDate;
    }

    public Boolean getisActive() {
        return isActive;
    }
}
