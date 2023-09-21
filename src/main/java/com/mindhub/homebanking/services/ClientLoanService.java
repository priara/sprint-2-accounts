package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ClientLoanService {

    void clientLoanSave(ClientLoan clientLoan);

    boolean existsByClientAndLoan(Client client, Loan loan);

    ClientLoan findById(Long id);
}
