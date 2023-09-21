package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void clientLoanSave(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public boolean existsByClientAndLoan(Client client, Loan loan) {
        return clientLoanRepository.existsByClientAndLoan(client, loan);
    }

    @Override
    public ClientLoan findById(Long id) {
        return null;
    }


}
