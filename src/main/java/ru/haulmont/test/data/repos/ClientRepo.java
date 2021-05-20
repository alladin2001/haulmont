package ru.haulmont.test.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.entity.LoanOffer;

import java.util.List;
import java.util.Set;

public interface ClientRepo extends JpaRepository<Client, Integer> {
    List<Client> findByBanksNotContains(Bank bank);
}