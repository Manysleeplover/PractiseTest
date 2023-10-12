package ru.aston.romanov.practical.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aston.romanov.practical.services.operations.AccountOperation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AccountOperationsConfig {

    /**
     * Собирается мапа бинов-обработчиков для всех видов операций по счёту
     */
    @Bean
    public Map<String, AccountOperation> operations(List<AccountOperation> accountOperations) {
        return accountOperations.stream().collect(Collectors.toMap(AccountOperation::type, Function.identity()));
    }
}
