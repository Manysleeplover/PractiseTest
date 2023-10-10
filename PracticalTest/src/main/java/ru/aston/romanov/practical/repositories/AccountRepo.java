package ru.aston.romanov.practical.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.romanov.practical.entities.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
