package com.example.samplecrud.service;

import com.example.samplecrud.model.dto.AccountDto;
import com.example.samplecrud.model.dto.CreateAccountRequest;
import com.example.samplecrud.model.dto.EditUserAccountRequest;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDto> findAllByUserId(Long userId);

    Optional<AccountDto> findByAccountId(Long userId,Long id);

    AccountDto save(Long userId,CreateAccountRequest account);

    AccountDto editUserInfo(Long userId,EditUserAccountRequest request);

    void delete(Long userId,Long id);
}
