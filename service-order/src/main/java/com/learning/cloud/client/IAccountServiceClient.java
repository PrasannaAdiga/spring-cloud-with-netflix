package com.learning.cloud.client;

import com.learning.cloud.client.impl.AccountServiceServiceClient;
import com.learning.cloud.entity.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "account-service", fallback = AccountServiceServiceClient.class)
public interface IAccountServiceClient {

    @PutMapping("/v1/accounts/{accountId}/withdraw/{amount}")
    public Account withdraw(@PathVariable("accountId") Long id, @PathVariable("amount") int amount);
}
