package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LoanService {

    List<LoanDTO> getLoans();

    Loan findByName(String name);

    void saveLoan(Loan loan);

    boolean existsByName(String name);


}
