package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {
	LocalDate today = LocalDate.now();

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			// save a couple of customers
			/*clientRepository.save(new Client("Jack", "Bauer", "jackbauer@gmail.com"));
			clientRepository.save(new Client("Chloe", "O'Brian", "chloebrian@gmail.com"));
			clientRepository.save(new Client("Kim", "Bauer", "kim@gmail.com"));
			clientRepository.save(new Client("David", "Palmer", "david@gmail.com"));*/



			Client melba = new Client("melba", "morel", "melba@gmail.com");
			clientRepository.save(melba);
			Account accountMelba1 = new Account("VIN001", LocalDate.now(), 5000);
			melba.addAccount(accountMelba1);
			accountRepository.save(accountMelba1);
			Account accountMelba2 = new Account("VIN002", this.today.plusDays(1), 7500);
			melba.addAccount(accountMelba2);
			accountRepository.save(accountMelba2);

			Client michelle = new Client("Michelle", "Dessler", "michelle@gmail.com");
			clientRepository.save(michelle);
			Account accountMichelle1 = new Account("VIN003",LocalDate.now(), 8500);
			michelle.addAccount(accountMichelle1);
			accountRepository.save(accountMichelle1);
			Account accountMichelle2 = new Account("VIN004", this.today.plusDays(1), 3800);
			accountRepository.save(accountMichelle2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1200, LocalDateTime.now(), accountMelba1, "Cinema");
			accountMelba1.addTransaction(transaction1);
			transactionRepository.save(transaction1);


			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -230, LocalDateTime.now(), accountMelba1, "Test Debit");
			accountMelba1.addTransaction(transaction2);
			transactionRepository.save(transaction2);


			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 880, this.today.plusDays(1).atStartOfDay(), accountMelba2, "Spa Life");
			accountMelba2.addTransaction(transaction3);
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -300, this.today.plusDays(1).atStartOfDay(), accountMelba2, "Test Debit");
			accountMelba2.addTransaction(transaction4);
			transactionRepository.save(transaction4);

			/*Transaction transaction5 = new Transaction(TransactionType.CREDIT, 900, LocalDateTime.now(), accountMichelle1, "hola");
			accountMelba2.addTransaction(transaction5);
			transactionRepository.save(transaction5);

			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -980, LocalDateTime.now(), accountMichelle1, "hola");
			accountMelba2.addTransaction(transaction6);
			transactionRepository.save(transaction6);

			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 25, LocalDateTime.now(), accountMichelle2, "hola");
			accountMelba2.addTransaction(transaction7);
			transactionRepository.save(transaction7);

			Transaction transaction8 = new Transaction(TransactionType.DEBIT, 94, LocalDateTime.now(), accountMichelle2, "hola");
			accountMelba2.addTransaction(transaction8);
			transactionRepository.save(transaction8);*/

			// Actualizar las cuentas en la base de datos con las transacciones agregadas
			accountRepository.save(accountMelba1);
			accountRepository.save(accountMelba2);
			/*accountRepository.save(accountMichelle1);
			accountRepository.save(accountMichelle2);*/








		};


	}


}
