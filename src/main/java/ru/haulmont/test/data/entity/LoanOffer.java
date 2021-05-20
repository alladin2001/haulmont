package ru.haulmont.test.data.entity;

import ru.haulmont.test.data.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Table(name = "LOAN_OFFER")
@Entity
public class LoanOffer extends AbstractEntity {
    private Double sumLoan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "loanOffer", cascade = CascadeType.ALL)
    private Set<LoanPayment> loanPayments;


    public LoanOffer() {
    }

    public LoanOffer(Double sumLoan, Client client, Loan loan, Set<LoanPayment> loanPayments) {
        this.sumLoan = sumLoan;
        this.client = client;
        this.loan = loan;
        this.loanPayments = loanPayments;
    }

    public Double getSumLoan() {
        return sumLoan;
    }

    public void setSumLoan(Double sumLoan) {
        this.sumLoan = sumLoan;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Set<LoanPayment> getLoanPayments() {
        return loanPayments;
    }

    public void setLoanPayments(Set<LoanPayment> loanPayments) {
        this.loanPayments = loanPayments;
    }
}