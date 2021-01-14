package com.learning.cloud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.cloud.model.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {
    List<AccountDTO> findAllAccountByCustomerId(UUID customerId) throws JsonProcessingException;
}
