package com.learning.cloud.repository;

import com.learning.cloud.entity.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountRepository {
    private List<Account> accounts = new ArrayList<>();

    public Account add(Account account) {
        account.setId(String.valueOf(accounts.size() + 1));
        accounts.add(account);
        return account;
    }

    public Account update(Account account) {
        accounts.set(Integer.parseInt(account.getId()) - 1, account);
        return account;
    }

    public void deleteByAccountId(String id) {
        accounts.remove(Integer.parseInt(id) - 1);
    }

    public List<Account> findByIds(List<String> ids) {
        return accounts.stream().filter(a -> ids.contains(a.getNumber())).collect(Collectors.toList());
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accounts.stream().filter(a -> a.getNumber().equals(accountNumber)).findFirst();
    }

    public List<Account> findByCustomerId(String customerId) {
        return accounts.stream().filter(a -> a.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }
}
