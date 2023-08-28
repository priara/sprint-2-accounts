package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.controllers.AccountController.generateRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());

    }

    @RequestMapping(path = "/api/clients/current", method = RequestMethod.GET)
    public ClientDTO getClientCurrent(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        ClientDTO clientDTO = new ClientDTO(client);

        return clientDTO;

    }



    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));


    }
    @RequestMapping(path = "/api/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) !=  null) {


            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        String numeroAleatorio = "VIN-" + generateRandomNumber(); /*genera un número aleatorio único*/
        while(accountRepository.findByNumber(numeroAleatorio) != null){
            numeroAleatorio = generateRandomNumber();
        }
        Account account = new Account(numeroAleatorio, LocalDate.now(), 0);
        Client client = clientRepository.findByEmail(email);
        client.addAccount(account);
        accountRepository.save(account);
        clientRepository.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}


