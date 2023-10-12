package ru.aston.romanov.practical.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.aston.romanov.practical.dto.ErrorDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.NoBeneficiaryPresentException;

/**
 * Обработчики кастомных ошибок на уровне контроллера
 */
@RestControllerAdvice
public class CustomExceptionsControllerExceptionHandler {
    @ExceptionHandler(NoAccountPresentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleNoAccountPresent(RuntimeException ex) {
        return new ResponseEntity<>(buildErrorDTO(ex,HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoBeneficiaryPresentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleNoBeneficiaryPresent(RuntimeException ex) {
        return new ResponseEntity<>(buildErrorDTO(ex,HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPinCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleInvalidPinCode(RuntimeException ex) {
        return new ResponseEntity<>(buildErrorDTO(ex,HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleInvalidInsufficientFunds(RuntimeException ex) {
        return new ResponseEntity<>(buildErrorDTO(ex,HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()), HttpStatus.BAD_REQUEST);
    }


    private ErrorDTO buildErrorDTO(RuntimeException ex, int httpCode, String httpStatus){
        return ErrorDTO.builder()
                .message(ex.getMessage())
                .httpStatus(httpStatus)
                .httpCode(httpCode)
                .build();
    }
}
