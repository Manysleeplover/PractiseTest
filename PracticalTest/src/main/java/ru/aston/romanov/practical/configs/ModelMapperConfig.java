package ru.aston.romanov.practical.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper entityModelMapper() {
        return new ModelMapper();
    }
}
