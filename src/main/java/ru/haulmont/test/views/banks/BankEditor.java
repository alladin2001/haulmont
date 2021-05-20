package ru.haulmont.test.views.banks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.repos.BankRepo;
import ru.haulmont.test.views.dialog.ChangeHandler;

@SpringComponent
@UIScope
public class BankEditor extends Dialog{

    private final BankRepo bankRepo;
    private Bank bank;

    TextField name = new TextField("Name of bank");

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel);

    private Binder<Bank> binder = new Binder<>(Bank.class);
    private ChangeHandler changeHandler;

    public BankEditor(@Autowired BankRepo bankRepo) {
        this.bankRepo = bankRepo;
        VerticalLayout layout = new VerticalLayout();
        name.setRequiredIndicatorVisible(true);
        layout.add(new H2("Bank name"),name, actions);
        layout.setMargin(false);
        layout.setPadding(false);
        add(layout);
        binder.bindInstanceFields(this);
        binder.forField(name)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct name")
                .asRequired()
                .bind(Bank::getName, Bank::setName);
        save.getElement().getThemeList().add("primary");
        save.addClickListener(e -> save());
        cancel.addClickListener(e -> changeHandler.onChange());
    }

    public void editBank(Bank newBank) {
        if(newBank == null){
            changeHandler.onChange();
            return;
        }
        if(newBank.getId() != null){
            bank = bankRepo.findById(newBank.getId()).orElse(newBank);
        }else {
            bank = newBank;
        }
        binder.setBean(bank);
    }

    private void save() {
        if (binder.validate().isOk()){
            bankRepo.save(bank);
            changeHandler.onChange();
        }
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}
