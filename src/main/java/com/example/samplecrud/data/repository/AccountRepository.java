package com.example.samplecrud.data.repository;

import com.example.samplecrud.data.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findAllByUserId(Long userId);

    Optional<AccountEntity> findByUserIdAndId(Long userId, Long id);

}
