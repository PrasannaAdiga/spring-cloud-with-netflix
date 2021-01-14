package com.learning.cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.entity.Account;
import com.learning.cloud.exception.custom.ResourceNotFoundException;
import com.learning.cloud.model.AccountDTO;
import com.learning.cloud.repository.AccountRepository;
import com.learning.cloud.service.ICustomerService;
import com.learning.cloud.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final AccountRepository accountRepository;
    private final ModelMapperUtil modelMapperUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<AccountDTO> findAllAccountByCustomerId(UUID customerId) throws JsonProcessingException {
        List<AccountDTO> accountDTOS = new ArrayList<>();
        Iterable<Account> iAccounts = accountRepository.findAll();
        Stream<Account> accountStream = StreamSupport.stream(iAccounts.spliterator(), false);
        if (accountStream.count() <= 0) {
            log.error("ResourceNotFoundException is thrown: No account available for the given customer with id {}", customerId);
            throw new ResourceNotFoundException("No account available for the given customer with id " + customerId);
        }
        List<Account> accounts = accountStream.filter(account -> account.getCustomerId().equals(customerId)).collect(Collectors.toList());
        log.debug("Account found: {}", objectMapper.writeValueAsString(accounts.stream()));
        accounts.forEach(account -> accountDTOS.add((modelMapperUtil.convertToDTO(account))));
        return accountDTOS;
    }
}
