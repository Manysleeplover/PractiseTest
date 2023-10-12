package ru.aston.romanov.practical.constants;

import lombok.Getter;

/**
 * Enum для определения типов операций по счёту
 */
@Getter
public enum OperationEnum {
    WITHDRAW("WITHDRAW", 1),
    DEPOSIT("DEPOSIT", 2),
    TRANSFER("TRANSFER", 3);

    private final String name;
    private final int code;

    OperationEnum(String withdraw, int i) {
        this.name = withdraw;
        this.code = i;
    }

}
