package ru.haulmont.test.data.entity;

import ru.haulmont.test.data.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Table(name = "CLIENT")
@Entity
public class Client extends AbstractEntity {
    private String firstName;
    private String lastName;

    private String passportNumber;
    private String email;
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "client_banks",
    joinColumns = @JoinColumn(name = "client_id"),
    inverseJoinColumns = @JoinColumn(name = "bank_id"))
    private Set<Bank> banks;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<LoanOffer> loanOffers;

    public Client() {
    }

    public Client(String firstName, String lastName, String passportNumber, String email,
                  String phoneNumber, Set<Bank> banks, Set<LoanOffer> loanOffers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.banks = banks;
        this.loanOffers = loanOffers;
    }

    @PreRemove
    private void removeBanksFromClients() {
        for (Bank b : banks) {
            b.getClients().remove(this);
        }
    }

    public void addBank(Bank bank){
        banks.add(bank);
        bank.getClients().add(this);
    }

    public void removeBank(Bank bank){
        banks.remove(bank);
        bank.getClients().remove(this);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Bank> getBanks() {
        return banks;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }

    public Set<LoanOffer> getLoanOffers() {
        return loanOffers;
    }

    public void setLoanOffers(Set<LoanOffer> loanOffers) {
        this.loanOffers = loanOffers;
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName;
    }
}