package ru.aston.romanov.practical.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.romanov.practical.entities.Beneficiary;

import java.util.Optional;

public interface BeneficiaryRepo extends JpaRepository<Beneficiary, Long> {
    Optional<Beneficiary> findBeneficiariesByFirstnameAndLastnameAndPin(String firstname, String lastname, String pin);
}
