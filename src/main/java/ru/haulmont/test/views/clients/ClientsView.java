package ru.haulmont.test.views.clients;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.entity.LoanOffer;
import ru.haulmont.test.data.repos.BankRepo;
import ru.haulmont.test.data.repos.ClientRepo;
import ru.haulmont.test.data.repos.LoanOfferRepo;
import ru.haulmont.test.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "clients", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Clients")
public class ClientsView extends Div {

    private final ClientRepo clientRepo;
    private final BankRepo bankRepo;
    private final LoanOfferRepo loanOfferRepo;

    private final Grid<Client> clientGrid = new Grid<>(Client.class, false);
    private final Grid<Bank> bankGrid = new Grid<>(Bank.class, false);
    private final Grid<LoanOffer> loanOfferGrid = new Grid<>(LoanOffer.class, false);

    private final Button addClient = new Button("Add client");
    private final Button addBank = new Button("Add bank");
    private final Button addLoanOffer = new Button("Add loan");

    private final Button deleteClient = new Button("Delete");
    private final Button deleteBank = new Button("Delete");
    private final Button deleteLoanOffer = new Button("Close loan");

    private final Button editClient = new Button("Edit");

    private final ClientAddEditor clientAddEditor;
    private final ClientAddBank clientAddBank;
    private final ClientAddOffer clientAddOffer;

    @Autowired
    public ClientsView(ClientRepo clientRepo, BankRepo bankRepo, LoanOfferRepo loanOfferRepo,
                       ClientAddEditor clientAddEditor, ClientAddBank clientAddBank, ClientAddOffer clientAddOffer) {
        addClassName("clients-view");
        this.clientAddOffer = clientAddOffer;
        this.clientAddBank = clientAddBank;
        this.clientAddEditor = clientAddEditor;
        this.clientRepo = clientRepo;
        this.bankRepo = bankRepo;
        this.loanOfferRepo = loanOfferRepo;

        HorizontalLayout layout = new HorizontalLayout();
        createClientLayout(layout);
        createOtherLayouts(layout);
        createListeners();
        add(layout);
        show();
    }



    //config
    private void configClientLayout(){
        clientGrid.addColumn(Client::getFirstName).setSortable(true).setKey("firstName").setHeader("First name");
      //  clientGrid.addColumn(Client::getSecondName).setSortable(true).setKey("secondName").setHeader("Second name");
        clientGrid.addColumn(Client::getLastName).setSortable(true).setKey("lastName").setHeader("Last name");
        clientGrid.addColumn(Client::getEmail).setSortable(true).setKey("email").setHeader("E-mail");
        clientGrid.addColumn(Client::getPhoneNumber).setSortable(true).setKey("phoneNumber").setHeader("Phone number");
        clientGrid.addColumn(Client::getPassportNumber).setSortable(true).setKey("passportNumber").setHeader("Passport number");
        clientGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        clientGrid.removeThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);

        addClient.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editClient.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        editClient.setEnabled(false);
        deleteBank.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteClient.setEnabled(false);
    }
    private void configBankLayout() {
        bankGrid.addColumn(Bank::getName).setSortable(true).setKey("name").setHeader("Bank name");
        bankGrid.getColumnByKey("name").setTextAlign(ColumnTextAlign.CENTER);
        bankGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        bankGrid.removeThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        bankGrid.setEnabled(false);

        addBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBank.setEnabled(false);
        deleteBank.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteBank.setEnabled(false);
    }
    private void configLoansLayout() {
        loanOfferGrid.addColumn(LoanOffer::getSumLoan).setHeader("Sum of loan").setKey("sumLoan");
        loanOfferGrid.addColumn(loanOffer -> loanOffer.getLoan().getBank().getName()).setHeader("Bank").setKey("bank");
        loanOfferGrid.addColumn(loanOffer -> loanOffer.getLoan().getPercent()).setKey("percent").setHeader("Percent");

        addLoanOffer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addLoanOffer.setEnabled(false);
        deleteLoanOffer.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteLoanOffer.setEnabled(false);
    }

    private void createListeners(){
        addClient.addClickListener(e -> addEditClient(new Client()));
        clientAddBank.setChangeHandler(() -> {
            show();
        });
        addBank.addClickListener(e -> {
            if(bankRepo.findByClientsNotContains(clientGrid.asSingleSelect().getValue()).size() == 0) {
                Button btn =  new Button("Ok");
                Dialog dialog= new Dialog(new Text("Please, create more banks"), btn);
                btn.addClickListener(event -> dialog.close());
            }
            else {
                clientAddBank.open();
                clientAddBank.editClient(clientGrid.asSingleSelect().getValue());
            }
        });
        deleteBank.addClickListener(e -> {
            bankGrid.asSingleSelect().getValue().removeClient(clientGrid.asSingleSelect().getValue());
            bankRepo.save(bankGrid.asSingleSelect().getValue());
            clientRepo.save(clientGrid.asSingleSelect().getValue());
            show();
            });
        editClient.addClickListener(e -> addEditClient(clientGrid.asSingleSelect().getValue()));
        deleteClient.addClickListener(e -> {
            clientRepo.delete(clientGrid.asSingleSelect().getValue());
            show();
        });
        clientAddEditor.setChangeHandler(() -> {
            clientAddEditor.close();
            show();
        });
        clientGrid.addItemDoubleClickListener(e -> addEditClient(e.getItem()));

        clientGrid.addSelectionListener(valueChangeEvent -> {
            if (!clientGrid.asSingleSelect().isEmpty()) {
                bankGrid.setEnabled(true);
                loanOfferGrid.setEnabled(true);
                editClient.setEnabled(true);
                deleteClient.setEnabled(true);
                bankGrid.setItems(clientGrid.asSingleSelect().getValue().getBanks());
                loanOfferGrid.setItems(loanOfferRepo.findByClient(clientGrid.asSingleSelect().getValue()));
                addBank.setEnabled(true);
                addLoanOffer.setEnabled(true);
            } else {
                editClient.setEnabled(false);
                deleteClient.setEnabled(false);
                bankGrid.setEnabled(false);
                loanOfferGrid.setEnabled(false);
                addBank.setEnabled(false);
                addLoanOffer.setEnabled(false);
            }
        });
        bankGrid.asSingleSelect().addValueChangeListener(event -> {
            if (!bankGrid.asSingleSelect().isEmpty())
                deleteBank.setEnabled(true);
            else
                deleteBank.setEnabled(false);
        });
        loanOfferGrid.asSingleSelect().addValueChangeListener(event -> {
            if(!loanOfferGrid.asSingleSelect().isEmpty())
                deleteLoanOffer.setEnabled(true);
            else
                deleteLoanOffer.setEnabled(false);
        });
        addLoanOffer.addClickListener(e-> addOffer(clientGrid.asSingleSelect().getValue(), new LoanOffer()));
        deleteLoanOffer.addClickListener(event -> {
            if(!loanOfferGrid.asSingleSelect().isEmpty()){
                loanOfferRepo.delete(loanOfferGrid.asSingleSelect().getValue());
                show();
            }
        });
    }

    private void addEditClient(Client client) {
        clientAddEditor.open();
        clientAddEditor.editClient(client);

    }

    private void addOffer(Client client, LoanOffer offer){
        clientAddOffer.open();
        clientAddOffer.newOffer(client, offer);
        clientAddOffer.setChangeHandler(() -> {clientAddOffer.close(); show();});
    }

    //create
    private void createClientLayout(HorizontalLayout horizontalLayout){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.add(clientGrid);
        createButtonLayout(verticalLayout, addClient, editClient, deleteClient);
        horizontalLayout.add(verticalLayout);
        configClientLayout();
        verticalLayout.setWidth("70%");
    }
    private void createOtherLayouts(HorizontalLayout horizontalLayout){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        createBanksLayout(verticalLayout);
        createLoansLayout(verticalLayout);
        horizontalLayout.add(verticalLayout);
        verticalLayout.setWidth("30%");
    }
    private void createBanksLayout(VerticalLayout verticalLayout) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.add(bankGrid);
        createButtonLayout(layout, addBank, deleteBank);
        verticalLayout.add(layout);
        configBankLayout();
    }
    private void createLoansLayout(VerticalLayout verticalLayout) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.add(loanOfferGrid);
        createButtonLayout(layout, addLoanOffer, deleteLoanOffer);
        verticalLayout.add(layout);
        configLoansLayout();
    }

    //create buttons layout
    private void createButtonLayout(VerticalLayout verticalLayout, Button... buttons) {
        HorizontalLayout horizontalLayout = new HorizontalLayout(buttons);
        verticalLayout.add(horizontalLayout);
    }

    private void show(){
        if(!clientGrid.asSingleSelect().isEmpty()){
            bankGrid.setItems(clientGrid.asSingleSelect().getValue().getBanks());
            loanOfferGrid.setItems(loanOfferRepo.findByClient(clientGrid.asSingleSelect().getValue()));
            clientGrid.setItems(clientRepo.findAll());
        }
        else {
            clientGrid.setItems(clientRepo.findAll());
        }


    }
}
