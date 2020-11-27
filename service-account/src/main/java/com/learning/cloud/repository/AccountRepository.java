package com.learning.cloud.repository;

import com.learning.cloud.entity.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountRepository {
    private List<Account> accounts = new ArrayList<>();

    public Account add(Account account) {
        account.setId((long) accounts.size() + 1);
        accounts.add(account);
        return account;
    }

    public Account update(Account account) {
        accounts.set(account.getId().intValue() - 1, account);
        return account;
    }

    public void delete(Long id) {
        accounts.remove(id.intValue() - 1);
    }

    public List<Account> find(List<Long> ids) {
        return accounts.stream().filter(a -> ids.contains(a.getId())).collect(Collectors.toList());
    }

    public Account findById(Long id) {
        Optional<Account> sAccount = accounts.stream().filter(a -> a.getId().equals(id)).findFirst();
        if(sAccount.isPresent()) {
            return sAccount.get();
        } else {
            return null;
        }
    }

    public List<Account> findByCustomer(Long customerId) {
        return accounts.stream().filter(a -> a.getCustomerId().equals(customerId)).collect(Collectors.toList());
    }
}
