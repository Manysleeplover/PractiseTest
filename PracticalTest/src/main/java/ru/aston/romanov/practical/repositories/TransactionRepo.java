package ru.aston.romanov.practical.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.romanov.practical.entities.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
