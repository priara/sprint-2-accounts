package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.stereotype.Service;


public interface CardsService {

    void saveCard(Card card);

    Card findById(Long id);

    boolean existsByIdAndClient_Id(Long id,Long clientId);

    boolean existsByClientAndColorAndTypeAndIsActive(Client client, CardColor color, CardType type, Boolean isActive);
}
