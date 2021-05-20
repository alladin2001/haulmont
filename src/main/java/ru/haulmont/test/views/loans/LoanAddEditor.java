package ru.haulmont.test.views.loans;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.entity.Loan;
import ru.haulmont.test.data.repos.BankRepo;
import ru.haulmont.test.data.repos.LoanRepo;
import ru.haulmont.test.views.dialog.ChangeHandler;



@SpringComponent
@UIScope
public class LoanAddEditor extends Dialog {
    private final LoanRepo loanRepo;
    private final BankRepo bankRepo;

    private Loan loan;

    TextField name = new TextField("Name");
    NumberField maxLimit = new NumberField("Max limit");
    NumberField percent = new NumberField("Percent");


    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel);

    Grid<Bank> bankGrid = new Grid<>(Bank.class, false);

    Binder<Loan> binder = new Binder<>(Loan.class);
    private ChangeHandler changeHandler;


    public LoanAddEditor(LoanRepo loanRepo, BankRepo bankRepo) {
        this.loanRepo = loanRepo;
        this.bankRepo = bankRepo;

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        name.setRequiredIndicatorVisible(true);
        maxLimit.setRequiredIndicatorVisible(true);
        percent.setRequiredIndicatorVisible(true);

        add(new VerticalLayout(name, maxLimit, percent),bankGrid , actions);
        bankGrid.addColumn(Bank::getName).setHeader("Bank name");

        createListener();
    }

    private void createListener() {
        save.addClickListener(e -> save());
        cancel.addClickListener(e -> {changeHandler.onChange();close();});

        binder.forField(name)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct first name")
                .asRequired()
                .bind(Loan::getName, Loan::setName);

        binder.forField(maxLimit)
                .withValidator(s -> s != null , "Please, write correct second name")
                .asRequired()
                .bind(Loan::getMaxLimit, Loan::setMaxLimit);

        binder.forField(percent)
                .withValidator(s -> s != null, "Please, write correct email")
                .asRequired()
                .bind(Loan::getPercent, Loan::setPercent);
    }

    public void editClient(Loan newLoan) {
        if(newLoan == null){
            changeHandler.onChange();
            return;
        }
        if(newLoan.getId() != null){
            loan = loanRepo.findById(newLoan.getId()).orElse(newLoan);
        }else {
            loan = newLoan;
        }
        bankGrid.setItems(bankRepo.findAll());
        binder.setBean(loan);
    }

    private void save() {
        if (binder.validate().isOk() && !bankGrid.asSingleSelect().isEmpty()){
            loan.setBank(bankGrid.asSingleSelect().getValue());
            loanRepo.save(loan);
            changeHandler.onChange();
            bankGrid.asSingleSelect().clear();
            close();
        }
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}
