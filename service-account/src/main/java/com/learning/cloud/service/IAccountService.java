package com.learning.cloud.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.model.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface IAccountService {
    AccountDTO findByAccountNumber(String accountNumber);
    Iterable<AccountDTO> findByAccountIds(List<UUID> accountIds);
    AccountDTO add(AccountDTO accountDTO);
    AccountDTO update(AccountDTO accountDTO);
    void deleteByAccountId(UUID id);
    AccountDTO withdraw(String accountNumber, int amount) throws JsonProcessingException;
}
