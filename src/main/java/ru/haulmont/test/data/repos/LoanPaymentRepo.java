package ru.haulmont.test.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.haulmont.test.data.entity.LoanOffer;
import ru.haulmont.test.data.entity.LoanPayment;

import java.util.List;

public interface LoanPaymentRepo extends JpaRepository<LoanPayment, Integer> {
    List<LoanPayment> findByLoanOffer(LoanOffer loanOffer);
}