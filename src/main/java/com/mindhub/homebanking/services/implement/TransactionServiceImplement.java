package com.mindhub.homebanking.services.implement;

import java.util.List;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public void transactionSave(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    public List<Transaction> findByDateBetweenAndAccount_Number(LocalDateTime localDateTime, LocalDateTime localDateTime2, String numberAcc) {
        return transactionRepository.findByDateBetweenAndAccount_Number(localDateTime,localDateTime2,numberAcc);
    }


}
