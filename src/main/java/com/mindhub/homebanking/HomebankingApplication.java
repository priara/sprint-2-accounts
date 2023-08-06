package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			// save a couple of customers
			/*clientRepository.save(new Client("Jack", "Bauer", "jackbauer@gmail.com"));
			clientRepository.save(new Client("Chloe", "O'Brian", "chloebrian@gmail.com"));
			clientRepository.save(new Client("Kim", "Bauer", "kim@gmail.com"));
			clientRepository.save(new Client("David", "Palmer", "david@gmail.com"));
			clientRepository.save(new Client("Michelle", "Dessler", "michelle@gmail.com"));*/

			Client melba = new Client("melba", "morel", "melba@gmail.com");
			clientRepository.save(melba);
			Account accountMelba = new Account("VIN001", LocalDate.now(), 5000);
			melba.addAccount(accountMelba);
			accountRepository.save(accountMelba);


			Account accountMelba2 = new Account("VIN002", this.today.plusDays(1), 7500);
			melba.addAccount(accountMelba2);
			accountRepository.save(accountMelba2);

		};


	}


}
