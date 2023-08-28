package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());

    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id) {
        return new AccountDTO(accountRepository.findById(id).orElse(null));


    }

    /*requestmapping: para mapear una solicitud HTTP a un método de controlador en una aplicación web.
    Esta anotación se utiliza para definir cómo se deben manejar
    las solicitudes HTTP entrantes y a qué método del controlador deben dirigirse.*/
    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        if (authentication != null) { /*si hay un cliente autenticado entra en la condicion*/
            Client client = clientRepository.findByEmail(authentication.getName());
            /*creo una variable y busco al cliente autenticado por el mail*/
            Set<Account> accounts = client.getAccounts();

            if (accounts.size() < 3) {
                String numeroAleatorio = "VIN-" + generateRandomNumber(); /*genere un numero aleatorio*/
                while(accountRepository.findByNumber(numeroAleatorio) !=null){ /*busque en la base de datos por el numero con numero aleatorio*/
                    numeroAleatorio = generateRandomNumber(); /*en caso de que ya exista un numero asociado a una cuenta, generamos otro numero aleatorio*/
                }
                Account account = new Account(numeroAleatorio, LocalDate.now(), 0);
                /*cree una cuenta nueva y se le asigno al cliente*/
                client.addAccount(account);
                accountRepository.save(account);
                clientRepository.save(client); /*si yo no guardo la cuenta al cliente, no se va a ver en la base de datos*/


                return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("The session expired", HttpStatus.FORBIDDEN);
        }
    }
    /*metodo para crear numero de cuenta aleatorio*/
    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000000);
        String randomString = Integer.toString(randomNumber); // Convierte el número a una cadena
        return randomString;
    }


}

