package com.example.samplecrud.service;

import com.example.samplecrud.data.entity.AccountEntity;
import com.example.samplecrud.data.repository.AccountRepository;
import com.example.samplecrud.exception.BadRequestException;
import com.example.samplecrud.exception.ResourceNotFoundException;
import com.example.samplecrud.model.dto.AccountDto;
import com.example.samplecrud.model.dto.CreateAccountRequest;
import com.example.samplecrud.model.dto.EditUserAccountRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<AccountDto> findAllByUserId(Long userId) {
        log.info("Fetching all accounts for userId={}", userId);
        try {
            return accountRepository.findAllByUserId(userId)
                    .stream()
                    .map(this::mapToDto)
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching accounts for userId={}", userId, e);
            throw new BadRequestException("Failed to fetch accounts");
        }
    }

    @Override
    public Optional<AccountDto> findByAccountId(Long userId,Long accountId) {
        log.info("Fetching account with id={} for userId={}", accountId, userId);
        return accountRepository.findByUserIdAndId(userId, accountId)
                .map(this::mapToDto)
                .or(() -> {
                    throw new ResourceNotFoundException("Account not found");
                });
    }

    @Override
    public AccountDto save(Long userId,CreateAccountRequest request) {
        log.info("Saving new account for userId={} with depositType={}", userId, request.getDepositType());
        try {
            AccountEntity accountEntity = mapToEntity(userId, request);
            AccountEntity saved = accountRepository.save(accountEntity);
            log.info("Account saved successfully: accountId={}", saved.getId());
            return mapToDto(saved);
        } catch (Exception e) {
            log.error("Error saving account for userId={}", userId, e);
            throw new BadRequestException("Failed to save account");
        }
    }

    @Transactional
    @Override
    public void delete(Long userId,Long accountId) {
        log.info("Deleting accountId={} for userId={}", accountId, userId);
        AccountEntity account = accountRepository.findByUserIdAndId(userId, accountId)
                .orElseThrow(() -> {
                    log.error("Account not found: accountId={} for userId={}", accountId, userId);
                    return new ResourceNotFoundException("Account not found or not owned by current user");
                });
        accountRepository.delete(account);
        log.info("Account deleted successfully: accountId={}", accountId);
    }


    private AccountDto mapToDto(AccountEntity accountEntity) {
        AccountDto dto = new AccountDto();
        dto.setUserId(accountEntity.getUserId());
        dto.setDepositNumber(accountEntity.getDepositNumber());
        dto.setDepositType(accountEntity.getDepositType());
        dto.setOwnerFullName(accountEntity.getOwnerFullName());
        return dto;
    }

    private AccountEntity mapToEntity(Long userId,CreateAccountRequest request) {
        if (request.getOwnerFullName() == null || request.getDepositType() == null) {
            log.warn("Invalid create account request for userId={}", userId);
            throw new BadRequestException("Owner name or deposit type cannot be null");
        }
        AccountEntity entity = new AccountEntity();
        entity.setUserId(userId);
        entity.setDepositNumber(request.getDepositType().getDepositNumber());
        entity.setDepositType(request.getDepositType());
        entity.setOwnerFullName(request.getOwnerFullName());
        return entity;
    }

    public AccountDto editUserInfo(Long userId,EditUserAccountRequest request) {
        log.info("Editing accountId={} for userId={}", request.getAccountId(), userId);
        AccountEntity account = accountRepository.findByUserIdAndId(userId, request.getAccountId())
                .orElseThrow(() -> {
                    log.error("Account not found: accountId={} for userId={}", request.getAccountId(), userId);
                    return new ResourceNotFoundException("Account not found");
                });
        account.setOwnerFullName(request.getOwnerFullName());
        AccountEntity updated = accountRepository.save(account);
        log.info("Account updated successfully: accountId={}", updated.getId());
        return mapToDto(updated);
    }
}
