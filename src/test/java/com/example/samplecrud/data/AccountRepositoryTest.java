package com.example.samplecrud.data;

import com.example.samplecrud.data.entity.AccountEntity;
import com.example.samplecrud.data.repository.AccountRepository;
import com.example.samplecrud.model.enumaration.DepositType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findAllByUserId_ShouldReturnOptionalAccount() {

        AccountEntity account = new AccountEntity();
        account.setUserId(1L);
        account.setDepositNumber("123456");
        account.setOwnerFullName("Ali Reza");
        account.setDepositType(DepositType.CURRENT);
        accountRepository.save(account);


        Optional<AccountEntity> result = accountRepository.findAllByUserId(1L);


        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(1L);
        assertThat(result.get().getDepositNumber()).isEqualTo("123456");
    }

    @Test
    void findByUserIdAndId_ShouldReturnOptionalAccount() {

        AccountEntity account = new AccountEntity();
        account.setUserId(2L);
        account.setDepositNumber("654321");
        account.setOwnerFullName("Sara Ahmadi");
        account.setDepositType(DepositType.SAVING);
        AccountEntity saved = accountRepository.save(account);


        Optional<AccountEntity> result = accountRepository.findByUserIdAndId(2L, saved.getId());


        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(2L);
        assertThat(result.get().getDepositNumber()).isEqualTo("654321");
    }

    @Test
    void findByUserIdAndId_ShouldReturnEmptyForWrongUser() {

        AccountEntity account = new AccountEntity();
        account.setUserId(3L);
        account.setDepositNumber("111222");
        account.setOwnerFullName("Test User");
        account.setDepositType(DepositType.SAVING);
        AccountEntity saved = accountRepository.save(account);


        Optional<AccountEntity> result = accountRepository.findByUserIdAndId(999L, saved.getId());


        assertThat(result).isEmpty();
    }
}
