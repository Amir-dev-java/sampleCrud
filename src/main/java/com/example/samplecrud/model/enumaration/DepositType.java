package com.example.samplecrud.model.enumaration;

import lombok.Getter;

@Getter
public enum DepositType {
    CURRENT(1, "1001008150643"),
    INTEREST_FREE(2, "7001008135674"),
    SAVING(3, "4001004132643");

    private final int value;
    private final String defaultDepositNumber;

    DepositType(int value, String defaultDepositNumber) {
        this.value = value;
        this.defaultDepositNumber = defaultDepositNumber;
    }

    public String getDepositNumber() { return defaultDepositNumber; }
}

