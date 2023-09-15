package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardsServiceImplement implements CardsService {

    @Autowired
    private CardRepository cardRepository;


    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByIdAndClient_Id(Long id, Long clientId) {
        return cardRepository.existsByIdAndClient_Id(id,clientId);
    }

    @Override
    public boolean existsByClientAndColorAndTypeAndIsActive(Client client, CardColor color, CardType type, Boolean isActive) {
        return cardRepository.existsByClientAndColorAndTypeAndIsActive(client, color, type, isActive);
    }


}
