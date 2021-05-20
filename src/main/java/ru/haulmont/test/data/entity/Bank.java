package ru.haulmont.test.data.entity;

import ru.haulmont.test.data.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Table(name = "BANK")
@Entity
public class Bank extends AbstractEntity {
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "client_banks",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Set<Client> clients;

    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Loan> loans;

    public Bank() {
    }

    public Bank(String name, Set<Client> clients, Set<Loan> loans) {
        this.name = name;
        this.clients = clients;
        this.loans = loans;
    }

    @PreRemove
    private void removeClientFromBanks() {
        for (Client c : clients) {
            c.getBanks().remove(this);
        }
    }

    public void addClient(Client client){
        clients.add(client);
        client.getBanks().add(this);
    }

    public void removeClient(Client client){
        clients.remove(client);
        client.getBanks().remove(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }
}