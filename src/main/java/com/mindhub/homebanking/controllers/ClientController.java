package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();

    }

    @GetMapping("/api/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication) {


        ClientDTO clientDTO = clientService.getClientDTO(authentication.getName());

        return clientDTO;

    }



    @GetMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientService.findById(id);


    }
    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientService.findByEmail(email) !=  null) {


            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));


        String numeroAleatorio = "VIN-" + generateRandomNumber(); /*genera un número aleatorio único*/
        while(accountService.findByNumber(numeroAleatorio) != null){
            numeroAleatorio = generateRandomNumber();
        }

        Account account = new Account(numeroAleatorio, LocalDate.now(), 0, true, AccountType.SAVINGS);


        accountService.addAccount(account);
        newClient.addAccount(account);
        clientService.saveClient(newClient);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}


