package com.learning.cloud.repository;

import com.learning.cloud.entity.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, UUID> {
    Optional<Account> findById(UUID id);
    Optional<Account> findByNumber(String number);
}
