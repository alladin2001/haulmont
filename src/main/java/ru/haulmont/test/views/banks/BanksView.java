package ru.haulmont.test.views.banks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import ru.haulmont.test.data.entity.Loan;
import ru.haulmont.test.data.repos.BankRepo;
import ru.haulmont.test.data.repos.ClientRepo;
import ru.haulmont.test.data.repos.LoanRepo;
import ru.haulmont.test.views.main.MainView;

@Route(value = "banks", layout = MainView.class)
@PageTitle("Banks")
public class BanksView extends Div {

    private Grid<Bank> bankGrid = new Grid<>(Bank.class, false);
    private Grid<Client> clientGrid = new Grid<>(Client.class, false);
    private Grid<Loan> loanGrid = new Grid<>(Loan.class, false);

    private Button addBank= new Button("Add");
   // private Button addClient = new Button("Add");
  //  private Button addLoan = new Button("Add");
   
    private Button deleteBank = new Button("Delete");
  //  private Button deleteClient = new Button("Delete");
   // private Button deleteLoan = new Button("Delete");
   
    private Button editBank = new Button("Edit");

    private final BankEditor bankEditor;

    private final BankRepo bankRepo;
    private final ClientRepo clientRepo;
    private final LoanRepo loanRepo;

    @Autowired
    public BanksView(BankEditor bankEditor, BankRepo bankRepo,
                     ClientRepo clientRepo, LoanRepo loanRepo) {
        addClassName("banks-view");
        this.bankEditor = bankEditor;
        this.bankRepo = bankRepo;
        this.clientRepo = clientRepo;
        this.loanRepo = loanRepo;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        bankEditor.setModal(true);
        addBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       // addClient.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       // addLoan.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        deleteBank.addThemeVariants(ButtonVariant.LUMO_ERROR);
      //  deleteClient.addThemeVariants(ButtonVariant.LUMO_ERROR);
      //  deleteLoan.addThemeVariants(ButtonVariant.LUMO_ERROR);

        editBank.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        createListeners();

        createMainGridLayout(horizontalLayout);
        createOtherGridsLayout(horizontalLayout);

        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        add(horizontalLayout);

        bankGrid.setItems(bankRepo.findAll());
        showAll();
    }

    private void createListeners() {
        addBank.addClickListener(buttonClickEvent -> addEditBank(new Bank()));
        editBank.addClickListener(buttonClickEvent -> addEditBank(bankGrid.asSingleSelect().getValue()));
        deleteBank.addClickListener(buttonClickEvent -> {
            bankRepo.delete(bankGrid.asSingleSelect().getValue());
            bankGrid.setItems(bankRepo.findAll());
            showAll();
        });
        bankGrid.addItemDoubleClickListener(event -> {
            addEditBank(event.getItem());
        });
        bankGrid.addSelectionListener(valueChangeEvent -> {

            if (!bankGrid.asSingleSelect().isEmpty()) {
                clientGrid.setEnabled(true);
                loanGrid.setEnabled(true);
                editBank.setEnabled(true);
                deleteBank.setEnabled(true);
              //  addClient.setEnabled(true);
           //     addLoan.setEnabled(true);

            } else {
                clientGrid.setEnabled(false);
                loanGrid.setEnabled(false);
                editBank.setEnabled(false);
                deleteBank.setEnabled(false);
              //  addClient.setEnabled(false);
             //   addLoan.setEnabled(false);
            }
            showAll();
        });
    }

    private void createMainGridLayout(HorizontalLayout horizontalLayout){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(bankGrid);
        bankGrid.addColumn(Bank::getName).setHeader("Bank name").setKey("name").setSortable(true);
        bankGrid.getColumnByKey("name").setTextAlign(ColumnTextAlign.CENTER);
        bankGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        createButtonsLayout(verticalLayout, addBank, editBank, deleteBank);
        verticalLayout.setMargin(false);
        verticalLayout.setPadding(false);
        verticalLayout.setWidth("30%");
        horizontalLayout.add(verticalLayout);
    }
    
    private void createOtherGridsLayout(HorizontalLayout horizontalLayout) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(clientGrid);
        clientGrid.addColumn(Client::toString).setHeader("Client name").setKey("name").setSortable(true);
        clientGrid.addColumn(Client::getPhoneNumber).setHeader("Phone number").setKey("phone").setSortable(true);
        clientGrid.addColumn(Client::getEmail).setHeader("E-mail").setKey("email").setSortable(true);
       // clientGrid.getColumnByKey("name").setTextAlign(ColumnTextAlign.CENTER);
        clientGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        createButtonsLayout(verticalLayout
                //,addClient, deleteClient
        );
        verticalLayout.add(loanGrid);
        loanGrid.addColumn(Loan::getName).setHeader("Loan name").setKey("name").setSortable(true);
        loanGrid.addColumn(Loan::getMaxLimit).setHeader("Max limit").setKey("limit").setSortable(true);
        loanGrid.addColumn(Loan::getPercent).setHeader("Percent, %").setKey("percent").setSortable(true);
        loanGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
       // createButtonsLayout(verticalLayout, addLoan, deleteLoan);
        verticalLayout.setMargin(false);
        verticalLayout.setPadding(false);
        verticalLayout.setWidth("70%");
        horizontalLayout.add(verticalLayout);
    }

    
    private void createButtonsLayout(VerticalLayout verticalLayout, Button... buttons){
        HorizontalLayout horizontalLayout = new HorizontalLayout(buttons);
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(false);
        verticalLayout.add(horizontalLayout);
    }

    private void showAll(){
        if (!bankGrid.asSingleSelect().isEmpty()) {
            clientGrid.setItems(bankGrid.asSingleSelect().getValue().getClients());
            loanGrid.setItems(bankGrid.asSingleSelect().getValue().getLoans());
        }
        else {
            clientGrid.setItems(clientRepo.findAll());
            loanGrid.setItems(loanRepo.findAll());
        }
    }

    private void addEditBank(Bank bank){
        bankEditor.open();
        bankEditor.editBank(bank);
        bankEditor.setChangeHandler(() -> {
                  bankEditor.close();
            bankGrid.setItems(bankRepo.findAll());
                  showAll();
        });
    }
}
