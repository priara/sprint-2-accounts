package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();

    }

    @GetMapping("/api/accounts/{id}")
    public ResponseEntity<Object> getAccounts(@PathVariable Long id) {
       Account account= accountService.findById(id);
        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.OK);


    }

    /*requestmapping: para mapear una solicitud HTTP a un método de controlador en una aplicación web.
    Esta anotación se utiliza para definir cómo se deben manejar
    las solicitudes HTTP entrantes y a qué método del controlador deben dirigirse.*/
    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication authentication) {

        if (authentication != null) { /*si hay un cliente autenticado entra en la condicion*/

            Client client = clientService.findByEmail(authentication.getName());
            /*creo una variable y busco al cliente autenticado por el mail*/
            Set<Account> accounts = client.getAccounts();

            List<Account> activeAccounts = client.getAccounts().stream()
                    .filter(Account::getActive)
                    .collect(Collectors.toList());

            if (activeAccounts.size() < 3) {
                String numeroAleatorio = "VIN-" + generateRandomNumber(); /*genere un numero aleatorio*/
                while(accountService.findByNumber(numeroAleatorio) !=null){ /*busque en la base de datos por el numero con numero aleatorio*/
                    numeroAleatorio = generateRandomNumber(); /*en caso de que ya exista un numero asociado a una cuenta, generamos otro numero aleatorio*/
                }
                Account account = new Account(numeroAleatorio, LocalDate.now(), 0, true, type);
                /*cree una cuenta nueva y se le asigno al cliente*/
                client.addAccount(account);
                accountService.addAccount(account);
                clientService.saveClient(client); /*si yo no guardo la cuenta al cliente, no se va a ver en la base de datos*/


                return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } else{
                return new ResponseEntity<>("You already have 3 accounts", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("The session expired", HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/api/clients/current/accounts/{id}")
    public ResponseEntity<Object> removeAccount(@PathVariable Long id, Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());


        if (client == null) {
            return new ResponseEntity<>("the client was not found", HttpStatus.UNAUTHORIZED);
        }

        if (id == null){
            return new ResponseEntity<>("the id was not found", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.findById(id);

        if (account == null){
            return new ResponseEntity<>("this account doesn't exist in the database", HttpStatus.FORBIDDEN);
        }

        if (client.getAccounts().size() <= 1){
            return new ResponseEntity<>("You can't because you have at least 1 account", HttpStatus.FORBIDDEN);
        }


        if (!account.getActive()){
            return new ResponseEntity<>("The account is not active", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByIdAndClient_Id(id, client.getId())){
            return new ResponseEntity<>("this account doesn't exist with that id", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() > 0){
            return new ResponseEntity<>("you can't remove an account with money", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.findByAccountId(id);
        if (transactions !=null){
            transactions.forEach(transaction ->{
                transaction.setActive(false);
                transactionService.transactionSave(transaction);
            });
        }


        account.setActive(false);
        accountService.addAccount(account);

        return new ResponseEntity<>("account removed succesfully", HttpStatus.OK);
    }


    /*metodo para crear numero de cuenta aleatorio*/
    public static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000000);
        String randomString = Integer.toString(randomNumber); // Convierte el número a una cadena
        return randomString;
    }


}

