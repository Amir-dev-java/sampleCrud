package com.example.samplecrud.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserAccountRequest {

    @NotNull(message = "error.validation.account.id.is.null")
    private Long accountId;

    @NotBlank(message = "error.validation.owner.name.is.blank")
    private String ownerFullName;
}
