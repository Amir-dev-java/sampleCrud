package com.example.samplecrud.service;

import com.example.samplecrud.data.entity.AccountEntity;
import com.example.samplecrud.data.repository.AccountRepository;
import com.example.samplecrud.exception.BadRequestException;
import com.example.samplecrud.exception.ResourceNotFoundException;
import com.example.samplecrud.model.dto.AccountDto;
import com.example.samplecrud.model.dto.CreateAccountRequest;
import com.example.samplecrud.model.dto.EditUserAccountRequest;
import com.example.samplecrud.model.enumaration.DepositType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final Long USER_ID = 10L;
    private final Long ACCOUNT_ID = 20L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AccountEntity createAccountEntity() {
        AccountEntity entity = new AccountEntity();
        entity.setId(ACCOUNT_ID);
        entity.setUserId(USER_ID);
        entity.setDepositNumber("12345");
        entity.setOwnerFullName("John Doe");
        entity.setDepositType(DepositType.CURRENT);
        return entity;
    }

    // ✅ findAllByUserId
    @Test
    void findAllByUserId_ShouldReturnListOfAccounts() {
        when(accountRepository.findAllByUserId(USER_ID))
                .thenReturn(Optional.of(createAccountEntity()));

        List<AccountDto> result = accountService.findAllByUserId(USER_ID);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getOwnerFullName()).isEqualTo("John Doe");
        verify(accountRepository).findAllByUserId(USER_ID);
    }

    // ✅ findByAccountId
    @Test
    void findByAccountId_ShouldReturnAccount_WhenFound() {
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.of(createAccountEntity()));

        Optional<AccountDto> result = accountService.findByAccountId(USER_ID, ACCOUNT_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getDepositNumber()).isEqualTo("12345");
    }

    @Test
    void findByAccountId_ShouldThrow_WhenNotFound() {
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findByAccountId(USER_ID, ACCOUNT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");
    }

    // ✅ save
    @Test
    void save_ShouldPersistAccountSuccessfully() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setDepositType(DepositType.CURRENT);
        request.setOwnerFullName("Alice");

        AccountEntity saved = createAccountEntity();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(saved);

        AccountDto result = accountService.save(USER_ID, request);

        assertThat(result.getOwnerFullName()).isEqualTo("John Doe");
        verify(accountRepository).save(any(AccountEntity.class));
    }

    @Test
    void save_ShouldThrow_WhenDepositTypeOrNameIsNull() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setDepositType(null);
        request.setOwnerFullName(null);
        assertThatThrownBy(() -> accountService.save(USER_ID, request))
                .isInstanceOf(BadRequestException.class);
    }

    // ✅ delete
    @Test
    void delete_ShouldRemoveAccount_WhenExists() {
        AccountEntity entity = createAccountEntity();
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.of(entity));

        accountService.delete(USER_ID, ACCOUNT_ID);

        verify(accountRepository).delete(entity);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.delete(USER_ID, ACCOUNT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found or not owned by current user");
    }

    // ✅ editUserInfo
    @Test
    void editUserInfo_ShouldUpdateOwnerName() {
        AccountEntity entity = createAccountEntity();
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.of(entity));
        when(accountRepository.save(any(AccountEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EditUserAccountRequest request = new EditUserAccountRequest();
        request.setAccountId(ACCOUNT_ID);
        request.setOwnerFullName("Updated Name");

        AccountDto result = accountService.editUserInfo(USER_ID, request);

        assertThat(result.getOwnerFullName()).isEqualTo("Updated Name");
        verify(accountRepository).save(any(AccountEntity.class));
    }

    @Test
    void editUserInfo_ShouldThrow_WhenAccountNotFound() {
        when(accountRepository.findByUserIdAndId(USER_ID, ACCOUNT_ID))
                .thenReturn(Optional.empty());

        EditUserAccountRequest request = new EditUserAccountRequest();
        request.setAccountId(ACCOUNT_ID);
        request.setOwnerFullName("Test");

        assertThatThrownBy(() -> accountService.editUserInfo(USER_ID, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");
    }
}
