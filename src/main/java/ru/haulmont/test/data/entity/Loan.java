package ru.haulmont.test.data.entity;

import ru.haulmont.test.data.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Table(name = "LOAN")
@Entity
public class Loan extends AbstractEntity {
    private String name;
    private Double maxLimit;
    private Double percent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LoanOffer> loanOffers;

    public Loan() {
    }

    public Loan(String name, Double maxLimit, Double percent, Bank bank, Set<LoanOffer> loanOffers) {
        this.name = name;
        this.maxLimit = maxLimit;
        this.percent = percent;
        this.bank = bank;
        this.loanOffers = loanOffers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Set<LoanOffer> getLoanOffers() {
        return loanOffers;
    }

    public void setLoanOffers(Set<LoanOffer> loanOffers) {
        this.loanOffers = loanOffers;
    }
}