package com.learning.cloud;

import com.learning.cloud.entity.Account;
import com.learning.cloud.repository.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ServiceAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAccountApplication.class, args);
	}

	@Bean
	AccountRepository repository() {
		AccountRepository repository = new AccountRepository();
		repository.add(new Account("1234567890", 50000, 1L));
		repository.add(new Account("1234567891", 50000, 1L));
		repository.add(new Account("1234567892", 50000, 1L));
		repository.add(new Account("1234567893", 50000, 2L));
		repository.add(new Account("1234567894", 50000, 2L));
		repository.add(new Account("1234567895", 50000, 2L));
		repository.add(new Account("1234567896", 50000, 3L));
		repository.add(new Account("1234567897", 50000, 3L));
		repository.add(new Account("1234567898", 50000, 3L));
		return repository;
	}
}
