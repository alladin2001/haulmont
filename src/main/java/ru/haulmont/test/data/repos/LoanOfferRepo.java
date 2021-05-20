package ru.haulmont.test.data.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.entity.LoanOffer;

import java.util.List;

public interface LoanOfferRepo extends JpaRepository<LoanOffer, Integer> {
    List<LoanOffer> findByClient(Client client);
}