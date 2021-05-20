package ru.haulmont.test.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.haulmont.test.data.entity.Loan;

public interface LoanRepo extends JpaRepository<Loan, Integer> {
}