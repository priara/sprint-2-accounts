package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.stereotype.Service;


public interface TransactionService {

    void transactionSave(Transaction transaction);
}
