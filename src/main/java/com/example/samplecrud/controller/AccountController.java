package com.example.samplecrud.controller;

import com.example.samplecrud.data.entity.UserEntity;
import com.example.samplecrud.data.repository.UserRepository;
import com.example.samplecrud.model.dto.AccountDto;
import com.example.samplecrud.model.dto.CreateAccountRequest;
import com.example.samplecrud.model.dto.EditUserAccountRequest;
import com.example.samplecrud.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Validated
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @GetMapping("/get-all-user-account")
    public ResponseEntity<List<AccountDto>> getAllAccounts(Principal principal) {
        UserEntity userEntity = userRepository
                .findByUsername(principal.getName()).orElseThrow(IllegalArgumentException::new);
        List<AccountDto> accounts = accountService.findAllByUserId(userEntity.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/get-account/{accountId}")
    public ResponseEntity<AccountDto> getAccount(Principal principal, @PathVariable Long accountId) {
        UserEntity userEntity = userRepository
                .findByUsername(principal.getName()).orElseThrow(IllegalArgumentException::new);
        AccountDto account = accountService.findByAccountId(userEntity.getId(), accountId)
                .orElseThrow();
        return ResponseEntity.ok(account);
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountDto> createAccount(Principal principal,
                                                    @RequestBody @Valid CreateAccountRequest request) {
        UserEntity userEntity = userRepository

                .findByUsername(principal.getName()).orElseThrow(IllegalArgumentException::new);
        AccountDto created = accountService.save(userEntity.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/edit-account/{accountId}")
    public ResponseEntity<AccountDto> editAccount(Principal principal,
            @PathVariable Long accountId,
            @RequestBody EditUserAccountRequest request) {
        UserEntity userEntity = userRepository
                .findByUsername(principal.getName()).orElseThrow(IllegalArgumentException::new);
        request.setAccountId(accountId);
        AccountDto updated = accountService.editUserInfo(userEntity.getId(), request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete-account/{accountId}")
    public ResponseEntity<Void> deleteAccount(Principal principal,@PathVariable Long accountId) {
        UserEntity userEntity = userRepository
                .findByUsername(principal.getName()).orElseThrow(IllegalArgumentException::new);
        accountService.delete(userEntity.getId(),accountId);
        return ResponseEntity.noContent().build();
    }
}
