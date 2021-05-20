package ru.haulmont.test.data.entity;

import ru.haulmont.test.data.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "LOAN_PAYMENT")
@Entity
public class LoanPayment extends AbstractEntity {
    private LocalDate date;
    private Double sumPayment;
    private Double sumPaymentLoan;
    private Double sumPaymentPercents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_offer_id")
    private LoanOffer loanOffer;

    public LoanPayment() {
    }

    public LoanPayment(LocalDate date, Double sumPayment, Double sumPaymentLoan,
                       Double sumPaymentPercents, LoanOffer loanOffer) {
        this.date = date;
        this.sumPayment = sumPayment;
        this.sumPaymentLoan = sumPaymentLoan;
        this.sumPaymentPercents = sumPaymentPercents;
        this.loanOffer = loanOffer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSumPayment() {
        return sumPayment;
    }

    public void setSumPayment(Double sumPayment) {
        this.sumPayment = sumPayment;
    }

    public Double getSumPaymentLoan() {
        return sumPaymentLoan;
    }

    public void setSumPaymentLoan(Double sumPaymentLoan) {
        this.sumPaymentLoan = sumPaymentLoan;
    }

    public Double getSumPaymentPercents() {
        return sumPaymentPercents;
    }

    public void setSumPaymentPercents(Double sumPaymentPercents) {
        this.sumPaymentPercents = sumPaymentPercents;
    }

    public LoanOffer getLoanOffer() {
        return loanOffer;
    }

    public void setLoanOffer(LoanOffer loanOffer) {
        this.loanOffer = loanOffer;
    }
}