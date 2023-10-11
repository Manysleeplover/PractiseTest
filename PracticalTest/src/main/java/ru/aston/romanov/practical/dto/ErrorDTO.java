package ru.aston.romanov.practical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {
    private String errorMessage;
    private String status;
    private String code;
}
