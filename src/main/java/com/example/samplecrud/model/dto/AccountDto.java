package com.example.samplecrud.model.dto;

import com.example.samplecrud.model.enumaration.DepositType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

    private Long userId;

    private String depositNumber;

    private String ownerFullName;

    private DepositType depositType;
}
