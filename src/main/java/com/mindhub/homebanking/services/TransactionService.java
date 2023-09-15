package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface TransactionService {

    void transactionSave(Transaction transaction);

    Set<Transaction> findByAccountId(Long accountId);

}
