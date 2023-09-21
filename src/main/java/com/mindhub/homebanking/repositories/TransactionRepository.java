package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Set<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByDateBetweenAndAccount_Number(LocalDateTime localDateTime, LocalDateTime localDateTime2, String numberAcc);


}
