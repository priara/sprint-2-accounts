package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardsService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;
import static com.mindhub.homebanking.utils.CardUtils.*;

@RestController
public class CardsController {


    @Autowired
    private ClientService clientService;

    @Autowired
    private CardsService cardsService;


    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards(

            @RequestParam CardColor color, @RequestParam CardType type, @RequestParam Boolean isActive, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientService.findByEmail(authentication.getName());


        CardType cardType = type;

        CardColor cardColor = color;


        // Antes de crear una nueva tarjeta
        boolean cardExists = cardsService.existsByClientAndColorAndTypeAndIsActive(client, color, type, isActive);

        if (cardExists) {
            return new ResponseEntity<>("You already have a card with this type and color", HttpStatus.FORBIDDEN);
        }
        // creo una nueva tarjeta
        int cvv = generateCvv();

        SecureRandom random = new SecureRandom();
        BigInteger maxLimit = new BigInteger("10000000000000000");
        BigInteger randomNumber = new BigInteger(maxLimit.bitLength(), random);

        // me aseguro de que el número generado sea menor que el límite máximo
        while (randomNumber.compareTo(maxLimit) >= 0) {
            randomNumber = new BigInteger(maxLimit.bitLength(), random);
        }

        String numberCard = (cardType == CardType.CREDIT) ? generateCreditNumber() : generateDebitNumber();
        String formattedNumberCard = numberCard.substring(0, 4) + "-" +
                numberCard.substring(4, 8) + "-" +
                numberCard.substring(8, 12) + "-" +
                numberCard.substring(12);
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusYears(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        String formattedDate = today.format(formatter);
        String formattedFutureDate = futureDate.format(formatter);


        Card newCard = new Card(cardHolder, cardType, cardColor, formattedNumberCard, cvv, formattedDate, formattedFutureDate, true);
        client.addCard(newCard);
        cardsService.saveCard(newCard);
        clientService.saveClient(client);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);

    }
    /*remover card*/
    @PatchMapping("/api/clients/current/cards/{id}")
    public ResponseEntity<Object> removeCards(@PathVariable Long id, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("the client was not found", HttpStatus.UNAUTHORIZED);
        }

        if (id == null){
            return new ResponseEntity<>("can't find that card", HttpStatus.FORBIDDEN);
        }

        Card card = cardsService.findById(id);

        if (card == null){
            return new ResponseEntity<>("the card was not found", HttpStatus.FORBIDDEN);
        }
        if (card.getisActive().toString().isBlank()){
            return new ResponseEntity<>("the card does not have a status", HttpStatus.FORBIDDEN);
        }
        if (!card.getisActive()){
            return new ResponseEntity<>("the card has already been removed", HttpStatus.FORBIDDEN);
        }

        if (!cardsService.existsByIdAndClient_Id(id,client.getId())){
            return new ResponseEntity<>("the card does not belong to the client", HttpStatus.FORBIDDEN);
        }


        card.setisActive(false);
        cardsService.saveCard(card);


        return new ResponseEntity<>("card removed succesfully", HttpStatus.OK);
    }



}



