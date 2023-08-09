package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> accounts = new HashSet<>();
    public ClientDTO() {

    }
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = new HashSet<>();
        for (Account account: client.getAccounts()) {
            this.accounts.add(new AccountDTO(account));
        }
    /*conviene usar map aca , no for each,para usar map tenemos que pasarlo a string con .string. despues .collect para transformarlo en
    * el dato que necesitamos en ese momento.*/
    }


    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }
}

/*
 El DTO sirve para modelar la info de la forma en la que queremos mostrarla ,si quiero que el mail sea secreto
 entonces con el dto solo pongo el cliente pero sin el mail, o por ejemplo para no mostrar contraseñas de un cliente por ejemplo
 eso es algo privado*/
