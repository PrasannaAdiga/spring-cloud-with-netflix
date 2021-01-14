package com.learning.cloud.util;

import com.learning.cloud.entity.Account;
import com.learning.cloud.model.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelMapperUtil {
    private final ModelMapper modelMapper;

    public AccountDTO convertToDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

    public Account convertToEntity(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }

}
