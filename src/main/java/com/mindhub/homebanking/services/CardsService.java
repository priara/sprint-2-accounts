package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import org.springframework.stereotype.Service;


public interface CardsService {

    void saveCard(Card card);
}
