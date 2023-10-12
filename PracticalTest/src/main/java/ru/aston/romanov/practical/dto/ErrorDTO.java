package ru.aston.romanov.practical.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private String message;
    private int httpCode;
    private String httpStatus;
}
