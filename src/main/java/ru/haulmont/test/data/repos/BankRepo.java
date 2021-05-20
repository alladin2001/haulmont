package ru.haulmont.test.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;

import java.util.List;
import java.util.Set;

public interface BankRepo extends JpaRepository<Bank, Integer> {
    List<Bank> findByClientsNotContains(Client client);
}