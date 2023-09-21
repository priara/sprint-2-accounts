package com.mindhub.homebanking.services;

import java.util.List;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


public interface TransactionService {

    void transactionSave(Transaction transaction);

    Set<Transaction> findByAccountId(Long accountId);


    List<Transaction> findByDateBetweenAndAccount_Number(LocalDateTime localDateTime, LocalDateTime localDateTime2, String numberAcc);

}
