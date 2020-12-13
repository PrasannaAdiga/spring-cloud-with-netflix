package com.learning.cloud;

import com.learning.cloud.entity.Account;
import com.learning.cloud.repository.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
@EnableEurekaClient
public class ServiceAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAccountApplication.class, args);
	}

	@Bean
	AccountRepository repository() {
		AccountRepository repository = new AccountRepository();
		repository.add(new Account("1234567890", new BigDecimal(50000), "1"));
		repository.add(new Account("1234567891", new BigDecimal(50000), "1"));
		repository.add(new Account("1234567892", new BigDecimal(50000), "1"));
		repository.add(new Account("1234567893", new BigDecimal(50000), "2"));
		repository.add(new Account("1234567894", new BigDecimal(50000), "2"));
		repository.add(new Account("1234567895", new BigDecimal(50000), "2"));
		repository.add(new Account("1234567896", new BigDecimal(50000), "3"));
		repository.add(new Account("1234567897", new BigDecimal(50000), "3"));
		repository.add(new Account("1234567898", new BigDecimal(50000), "3"));
		return repository;
	}
}
