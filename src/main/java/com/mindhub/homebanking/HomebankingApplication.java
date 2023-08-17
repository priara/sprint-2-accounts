package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	LocalDate today = LocalDate.now();
	LocalDate futureDate = today.plusYears(5);

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			/*clientRepository.save(new Client("Jack", "Bauer", "jackbauer@gmail.com"));
			clientRepository.save(new Client("Chloe", "O'Brian", "chloebrian@gmail.com"));
			clientRepository.save(new Client("Kim", "Bauer", "kim@gmail.com"));
			clientRepository.save(new Client("David", "Palmer", "david@gmail.com"));*/



			Client melba = new Client("Melba", "morel", "melba@gmail.com");
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

			/*LOAN/PRESTAMOS*/

			Loan mortgage = new Loan( "Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
			loanRepository.save(mortgage);
			Loan personal = new Loan( "Personal", 100000, Arrays.asList(6,12,24));
			loanRepository.save(personal);
			Loan auto = new Loan ("Auto", 300000, Arrays.asList( 6,12,24,36));
			loanRepository.save(auto);

			/*PRESTAMOS CLIENTE MELBA*/
			ClientLoan melbaLoan1 = new ClientLoan( 400000 , 60);
			melba.addClientLoan(melbaLoan1);
			mortgage.addClientLoan(melbaLoan1);
			clientLoanRepository.save(melbaLoan1);
			ClientLoan melbaLoan2 = new ClientLoan(50000, 12);
			melba.addClientLoan(melbaLoan2);
			personal.addClientLoan(melbaLoan2);
			clientLoanRepository.save(melbaLoan2);


			/*PRESTAMOS CLIENTE MICHELLE*/
			ClientLoan michelleLoan1 = new ClientLoan(100000, 24);
			michelle.addClientLoan(michelleLoan1);
			personal.addClientLoan(michelleLoan1);
			clientLoanRepository.save(michelleLoan1);
			ClientLoan michelleLoan2 = new ClientLoan(200000, 36);
			michelle.addClientLoan(michelleLoan2);
			auto.addClientLoan(michelleLoan2);
			clientLoanRepository.save(michelleLoan2);


			/*TARJETAS*/


			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
			String formattedDate = today.format(formatter);
			String formattedFutureDate = futureDate.format(formatter);

			Card melbaCard1 = new Card( "Melba " + "Morel", CardType.DEBIT , CardColor.GOLD, "5542-2050-8022-7454" , 888 , formattedDate , formattedFutureDate);
			melba.addCard(melbaCard1);
			cardRepository.save(melbaCard1);

			Card melbaCard2 = new Card("Melba " + "Morel",CardType.CREDIT , CardColor.TITANIUM, "4080-3055-7894-9010" , 412 , formattedDate , formattedFutureDate );
			melba.addCard(melbaCard2);
			cardRepository.save(melbaCard2);

			Card melbaCard3 = new Card("Melba " + "Morel",CardType.CREDIT , CardColor.SILVER, "5542-8541-2563-7452" , 565 , formattedDate , formattedFutureDate);
			melba.addCard(melbaCard3);
			cardRepository.save(melbaCard3);

			Card michelleCard1 = new Card("Michelle " + "Dessler", CardType.CREDIT , CardColor.SILVER, "5542-5264-3215-9856" , 931 , formattedDate , formattedFutureDate);
			michelle.addCard(michelleCard1);
			cardRepository.save(michelleCard1);

			/*TRANSACTIONS*/

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1200, LocalDateTime.now(), "Cinema");
			accountMelba1.addTransaction(transaction1);
			transactionRepository.save(transaction1);


			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -230, LocalDateTime.now(), "Test Debit");
			accountMelba1.addTransaction(transaction2);
			transactionRepository.save(transaction2);


			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 880, this.today.plusDays(1).atStartOfDay(), "Spa Life");
			accountMelba2.addTransaction(transaction3);
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -300, this.today.plusDays(1).atStartOfDay(), "Test Debit");
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










		};


	}


}
