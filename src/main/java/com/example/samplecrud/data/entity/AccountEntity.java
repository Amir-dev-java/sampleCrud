package com.example.samplecrud.data.entity;

import com.example.samplecrud.model.enumaration.DepositType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column(length = 20)
    private String depositNumber;

    @Column(length = 100)
    private String ownerFullName;

    @Column(length = 15)
    @Enumerated
    private DepositType depositType;
}


