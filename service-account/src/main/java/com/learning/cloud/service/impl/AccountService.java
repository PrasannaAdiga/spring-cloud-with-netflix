package com.learning.cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.cloud.entity.Account;
import com.learning.cloud.exception.custom.InsufficientBalanceException;
import com.learning.cloud.exception.custom.ResourceFoundException;
import com.learning.cloud.exception.custom.ResourceNotFoundException;
import com.learning.cloud.model.AccountDTO;
import com.learning.cloud.repository.AccountRepository;
import com.learning.cloud.service.IAccountService;
import com.learning.cloud.util.ModelMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final ModelMapperUtil modelMapperUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AccountDTO findByAccountNumber(String accountNumber) {
        Optional<Account> account = accountRepository.findByNumber(accountNumber);
        if(!account.isPresent()) {
            log.error("ResourceNotFoundException is thrown: Account with number {} Not Found!", accountNumber);
            throw new ResourceNotFoundException("Account with number " + accountNumber + " Not Found!");
        } else {
            return modelMapperUtil.convertToDTO(account.get());
        }
    }

    @Override
    public List<AccountDTO> findByAccountIds(List<UUID> accountIds) {
        Iterable<Account> accounts = accountRepository.findAllById(accountIds);
        if (StreamSupport.stream(accounts.spliterator(), false).count() <= 0) {
            log.error("ResourceNotFoundException is thrown: Account for the provided ids Not Found!");
            throw new ResourceNotFoundException("Account for the provided ids Not Found!");
        } else {
            List<AccountDTO> accountDTOS = new ArrayList<>();
            accounts.forEach(account -> {
                accountDTOS.add(modelMapperUtil.convertToDTO(account));
            });
            return accountDTOS;
        }
    }

    @Override
    public AccountDTO add(AccountDTO accountDTO) {
        Optional<Account> account = accountRepository.findByNumber(accountDTO.getNumber());
        if(account.isPresent()) {
            log.error("ResourceFoundException is thrown: Account with number {} already exists!", accountDTO.getId());
            throw new ResourceFoundException("Account with number " + accountDTO.getNumber() + " already exists");
        } else {
            Account createdAccount = accountRepository.save(modelMapperUtil.convertToEntity(accountDTO));
            return modelMapperUtil.convertToDTO(createdAccount);
        }
    }

    @Override
    public AccountDTO update(AccountDTO accountDTO) {
        Optional<Account> account = accountRepository.findById(accountDTO.getId());
        if(!account.isPresent()) {
            log.error("ResourceNotFoundException is thrown: Account with id {} not found!", accountDTO.getId());
            throw new ResourceNotFoundException("Account with id " + accountDTO.getId() + " Not Found!");
        } else {
            Account updateAccount = account.get();
            updateAccount.setBalance(accountDTO.getBalance());
            return modelMapperUtil.convertToDTO(accountRepository.save(updateAccount));
        }
    }

    @Override
    public void deleteByAccountId(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        if(!account.isPresent()) {
            log.error("ResourceNotFoundException is thrown: Account with id {} not found!", id);
            throw new ResourceNotFoundException("Account with id " + id + " Not Found!");
        } else {
            accountRepository.deleteById(id);
        }
    }

    @Override
    public AccountDTO withdraw(String accountNumber, int amount) throws JsonProcessingException {
        Optional<Account> account = accountRepository.findByNumber(accountNumber);
        if(!account.isPresent()) {
            log.error("ResourceNotFoundException is thrown: Account with number {} not found!", accountNumber);
            throw new ResourceNotFoundException("Account with number " + accountNumber + " Not Found!");
        } else {
            return finishWithdraw(account.get(), amount);
        }
    }

    private AccountDTO finishWithdraw(Account account, int amount) throws JsonProcessingException {
        log.debug("Account found: {}", objectMapper.writeValueAsString(account));
        if (account.getBalance().subtract(new BigDecimal(amount)).compareTo(BigDecimal.ZERO) < 0) {
            log.error("InsufficientBalanceException is thrown: Can not withdraw an amount of {} from account {}", amount, account.getNumber());
            throw new InsufficientBalanceException("Can not withdraw an amount of " + amount + " from account " + account.getNumber());
        }
        account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
        log.debug("Account balance: {}", objectMapper.writeValueAsString(Collections.singletonMap("balance", account.getBalance())));
        return modelMapperUtil.convertToDTO(accountRepository.save(account));
    }

}
