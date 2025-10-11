package com.example.samplecrud.controller;

import com.example.samplecrud.data.entity.UserEntity;
import com.example.samplecrud.data.repository.UserRepository;
import com.example.samplecrud.model.dto.AccountDto;
import com.example.samplecrud.model.dto.CreateAccountRequest;
import com.example.samplecrud.model.dto.EditUserAccountRequest;
import com.example.samplecrud.model.enumaration.DepositType;
import com.example.samplecrud.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountController accountController;

    private Principal principal;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = () -> "testUser";

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUser");
        userEntity.setPassword("pass");
        userEntity.setRoles("ROLE_USER");
    }

    // ---------- getAllAccounts ----------
    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() {
        AccountDto dto = new AccountDto();
        dto.setUserId(1L);
        dto.setDepositNumber("1234");
        dto.setOwnerFullName("Ali Reza");
        dto.setDepositType(DepositType.CURRENT);

        List<AccountDto> accounts = List.of(dto);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(accountService.findAllByUserId(1L)).thenReturn(accounts);

        ResponseEntity<List<AccountDto>> response = accountController.getAllAccounts(principal);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst().getDepositNumber()).isEqualTo("1234");
        verify(accountService).findAllByUserId(1L);
    }

    // ---------- getAccount ----------
    @Test
    void getAccount_ShouldReturnSingleAccount() {
        AccountDto dto = new AccountDto();
        dto.setUserId(1L);
        dto.setDepositNumber("4321");
        dto.setOwnerFullName("Sara");
        dto.setDepositType(DepositType.SAVING);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(accountService.findByAccountId(1L, 10L)).thenReturn(Optional.of(dto));

        ResponseEntity<AccountDto> response = accountController.getAccount(principal, 10L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getDepositNumber()).isEqualTo("4321");
        assertThat(response.getBody().getOwnerFullName()).isEqualTo("Sara");
        verify(accountService).findByAccountId(1L, 10L);
    }

    // ---------- createAccount ----------
    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setOwnerFullName("Ali Reza");
        request.setDepositType(DepositType.CURRENT);

        AccountDto created = new AccountDto();
        created.setUserId(1L);
        created.setDepositNumber("9999");
        created.setOwnerFullName("Ali Reza");
        created.setDepositType(DepositType.CURRENT);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(accountService.save(eq(1L), any(CreateAccountRequest.class))).thenReturn(created);

        ResponseEntity<AccountDto> response = accountController.createAccount(principal, request);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(response.getBody()).getOwnerFullName()).isEqualTo("Ali Reza");
        verify(accountService).save(eq(1L), any(CreateAccountRequest.class));
    }

    // ---------- editAccount ----------
    @Test
    void editAccount_ShouldReturnUpdatedAccount() {
        EditUserAccountRequest request = new EditUserAccountRequest();
        request.setAccountId(10L);
        request.setOwnerFullName("Updated Name");

        AccountDto updated = new AccountDto();
        updated.setUserId(1L);
        updated.setDepositNumber("5555");
        updated.setOwnerFullName("Updated Name");
        updated.setDepositType(DepositType.CURRENT);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));
        when(accountService.editUserInfo(eq(1L), any(EditUserAccountRequest.class))).thenReturn(updated);

        ResponseEntity<AccountDto> response = accountController.editAccount(principal, 10L, request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getOwnerFullName()).isEqualTo("Updated Name");
        verify(accountService).editUserInfo(eq(1L), any(EditUserAccountRequest.class));
    }

    // ---------- deleteAccount ----------
    @Test
    void deleteAccount_ShouldReturnNoContent() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        ResponseEntity<Void> response = accountController.deleteAccount(principal, 5L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(accountService).delete(1L, 5L);
    }
}
