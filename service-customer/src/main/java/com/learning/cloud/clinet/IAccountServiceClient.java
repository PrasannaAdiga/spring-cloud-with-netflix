package com.learning.cloud.clinet;

import com.learning.cloud.clinet.factory.AccountServiceClientFactory;
import com.learning.cloud.clinet.config.FeignConfig;
import com.learning.cloud.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gateway-server", fallbackFactory = AccountServiceClientFactory.class, configuration = FeignConfig.class)
public interface IAccountServiceClient {
    @GetMapping("account-service/v1/customers/{customerId}/accounts")
    List<Account> findAccountsByCustomerId(@PathVariable("customerId") Long id);
}
