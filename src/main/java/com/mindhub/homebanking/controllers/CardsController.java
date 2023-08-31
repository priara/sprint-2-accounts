package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.CardType.CREDIT;
import static com.mindhub.homebanking.models.CardType.DEBIT;

@RestController
public class CardsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCards(

            @RequestParam CardColor color, @RequestParam CardType type, Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        Set<Card> cards = client.getCards();

        if (cards.size() >= 6) {
            return new ResponseEntity<>("Maximum cards reached", HttpStatus.FORBIDDEN);
        }

        if (type != DEBIT && type != CREDIT){
            return new ResponseEntity<>("does not match any type", HttpStatus.FORBIDDEN);
        }
        CardType cardType = type;

        if (color != GOLD && color != PLATINUM && color != SILVER){
            return new ResponseEntity<>("does not match any colorr", HttpStatus.FORBIDDEN);
        }
        CardColor cardColor = color;


        // Antes de crear una nueva tarjeta
        boolean cardExists = client.getCards().stream()
                .anyMatch(card -> card.getType() == cardType && card.getColor() == cardColor);

        if (cardExists) {
            return new ResponseEntity<>("You already have a card with this type and color", HttpStatus.FORBIDDEN);
        } else {
            // creo una nueva tarjeta
            int cvv = generateCvv();
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

            Card newCard = new Card(cardHolder, cardType, cardColor, formattedNumberCard, cvv, formattedDate, formattedFutureDate);
            client.addCard(newCard);
            cardRepository.save(newCard);
            clientRepository.save(client);

            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
        }

    }

        public static int generateCvv () {
            Random random = new Random();
            int randomNumber = random.nextInt(999);
            return randomNumber;
        }

        public static String generateCreditNumber () {
            SecureRandom random = new SecureRandom();
            BigInteger maxLimit = new BigInteger("10000000000000000");
            BigInteger randomNumber = new BigInteger(maxLimit.bitLength(), random);

            // me aseguro de que el número generado sea menor que el límite máximo
            while (randomNumber.compareTo(maxLimit) >= 0) {
                randomNumber = new BigInteger(maxLimit.bitLength(), random);
            }

            return randomNumber.toString();
        }

        public static String generateDebitNumber () {
            SecureRandom random = new SecureRandom();
            BigInteger maxLimit = new BigInteger("10000000000000000");
            BigInteger randomNumber = new BigInteger(maxLimit.bitLength(), random);

            // me aseguro de que el número generado sea menor que el límite máximo
            while (randomNumber.compareTo(maxLimit) >= 0) {
                randomNumber = new BigInteger(maxLimit.bitLength(), random);
            }

            return randomNumber.toString();
        }




    }



