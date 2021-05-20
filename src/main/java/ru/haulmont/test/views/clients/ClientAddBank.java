package ru.haulmont.test.views.clients;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.repos.BankRepo;
import ru.haulmont.test.data.repos.ClientRepo;
import ru.haulmont.test.views.dialog.ChangeHandler;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class ClientAddBank extends Dialog {

    private final BankRepo bankRepo;
    private final ClientRepo clientRepo;
    private Client client;

    private Grid<Bank> bankGrid = new Grid<>(Bank.class, false);
    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private HorizontalLayout actions = new HorizontalLayout(save, cancel);

    private ChangeHandler changeHandler;

    public ClientAddBank(@Autowired BankRepo bankRepo, ClientRepo clientRepo) {
        this.bankRepo = bankRepo;
        this.clientRepo = clientRepo;
        VerticalLayout layout = new VerticalLayout();

        layout.setMargin(false);
        layout.setPadding(false);
        layout.add(new H2("Add Bank"), bankGrid,actions);
        add(layout);
        bankGrid.addColumn(Bank::getName).setHeader("Bank name").setKey("name");
        createValidator();
    }

    private void createValidator() {
        cancel.addClickListener(e -> {changeHandler.onChange();close();});
        save.getElement().getThemeList().add("primary");
        save.addClickListener(e -> save());
    }

    public void editClient(Client newClient) {
        client = newClient;
        bankGrid.setItems(bankRepo.findByClientsNotContains(client));
        bankGrid.asSingleSelect().setValue(bankRepo.findByClientsNotContains(client).get(0));
    }

    private void save() {
            if (!bankGrid.asSingleSelect().isEmpty()) {
                client.addBank(bankGrid.asSingleSelect().getValue());
                clientRepo.save(client);
                changeHandler.onChange();
                bankGrid.asSingleSelect().clear();
                close();
            }
    }


    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

}


