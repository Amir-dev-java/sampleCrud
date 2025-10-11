package com.example.samplecrud.model.dto;

import com.example.samplecrud.model.enumaration.DepositType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "error.validation.owner.name.is.blank")
    private String ownerFullName;

    @NotNull(message = "error.validation.deposit.type.is.blank")
    private DepositType depositType;

}
