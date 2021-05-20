package ru.haulmont.test.views.clients;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.entity.Loan;
import ru.haulmont.test.data.entity.LoanOffer;
import ru.haulmont.test.data.entity.LoanPayment;
import ru.haulmont.test.data.repos.*;
import ru.haulmont.test.views.dialog.ChangeHandler;

import java.time.LocalDate;

@SpringComponent
@UIScope
public class ClientAddOffer extends Dialog {

    private final BankRepo bankRepo;
    private final ClientRepo clientRepo;
    private final LoanRepo loanRepo;
    private final LoanPaymentRepo loanPaymentRepo;
    private final LoanOfferRepo loanOfferRepo;



    private Client client;
    private LoanOffer loanOffer;

    private Grid<Loan> loanGrid = new Grid<>(Loan.class, false);
    private DatePicker datePicker = new DatePicker("Date");
    private NumberField field = new NumberField("Month number");
    private NumberField numberField = new NumberField("Sum of loan");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");

    private TextField textField = new TextField("Percent sum");
    private TextField text = new TextField("Payment");

    ChangeHandler changeHandler;

    @Autowired
    public ClientAddOffer(BankRepo bankRepo, LoanRepo loanRepo, ClientRepo clientRepo, LoanPaymentRepo loanPaymentRepo, LoanOfferRepo loanOfferRepo) {
        this.bankRepo = bankRepo;
        this.clientRepo = clientRepo;
        this.loanRepo = loanRepo;
        this.loanPaymentRepo = loanPaymentRepo;
        this.loanOfferRepo = loanOfferRepo;
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(loanGrid, datePicker, field, numberField);
        datePicker.setRequired(true);
        numberField.setRequiredIndicatorVisible(true);
        numberField.setHasControls(true);
        numberField.setMin(0);
        field.setRequiredIndicatorVisible(true);
        field.setMin(3.0);
        field.setHasControls(true);

        loanGrid.addColumn(Loan::getName).setSortable(true).setHeader("Name");
        loanGrid.addColumn(Loan::getMaxLimit).setSortable(true).setHeader("Limit");
        loanGrid.addColumn(Loan::getPercent).setSortable(true).setHeader("Percent,%");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(verticalLayout);
        add(new HorizontalLayout(save, cancel));
        createListeners();
        loanGrid.setItems(loanRepo.findAll());
        this.setWidth("500px");
    }

    public void newOffer(Client newClient, LoanOffer loanOffer) {
        client = newClient;
        this.loanOffer = loanOffer;
    }

    private void save() {
        if(!loanGrid.asSingleSelect().isEmpty()) {
         //   client.addBank(loanGrid.asSingleSelect().getValue().getBank());
        //    loanGrid.asSingleSelect().getValue().getBank().addClient(client);
        //    bankRepo.save(loanGrid.asSingleSelect().getValue().getBank());
            loanOffer.setSumLoan(numberField.getValue());
            loanOffer.setLoan(loanGrid.asSingleSelect().getValue());
            loanOffer.setClient(client);
            loanOfferRepo.save(loanOffer);
            client.getLoanOffers().add(loanOffer);
            clientRepo.save(client);

            createSchedule();
        }
    }

    private void createSchedule() {
        double percent = loanOffer.getLoan().getPercent();
        int moths = field.getValue().intValue();
        double sum = numberField.getValue();
        LocalDate localDate = datePicker.getValue();

        double k = sum *(percent/1200+(percent/1200/(Math.pow(1+percent/1200,moths)-1)));
        for (int i =1; i <= moths; i++){
            loanPaymentRepo.save(new LoanPayment(localDate.plusDays(30*i), k, k - sum*percent/1200, sum*percent/1200, loanOffer));
            sum -=k - sum*percent/1200;
        }
    }


    private void createListeners() {
        loanGrid.addItemClickListener(loanItemClickEvent -> numberField.setMax(loanItemClickEvent.getItem().getMaxLimit()));
        save.addClickListener(e -> {save(); close();});
        cancel.addClickListener(event -> changeHandler.onChange());
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }



}
