package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByClientAndColorAndTypeAndIsActive(Client client, CardColor color, CardType type, Boolean isActive);

    boolean existsByIdAndClient_Id(Long id, Long clientId);
}
