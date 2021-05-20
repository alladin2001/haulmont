package ru.haulmont.test.views.clients;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Client;
import ru.haulmont.test.data.repos.ClientRepo;
import ru.haulmont.test.views.dialog.ChangeHandler;

@SpringComponent
@UIScope
public class ClientAddEditor extends Dialog {

    private final ClientRepo clientRepo;
    private Client client;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("E-mail");
    TextField phoneNumber = new TextField("Phone number");
    TextField passportNumber = new TextField("Passport number");

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel);

    private Binder<Client> binder = new Binder<>(Client.class);
    private ChangeHandler changeHandler;

    public ClientAddEditor(@Autowired ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
        VerticalLayout layout = new VerticalLayout();
        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        phoneNumber.setRequiredIndicatorVisible(true);
        passportNumber.setRequiredIndicatorVisible(true);
        layout.setMargin(false);
        layout.setPadding(false);
        layout.add(new H2("Create/Edit client"),firstName, lastName, email, phoneNumber, passportNumber, actions);
        add(layout);
        binder.bindInstanceFields(this);
        createValidator();
    }

    private void createValidator() {
        binder.forField(firstName)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct first name")
                .asRequired()
                .bind(Client::getFirstName, Client::setFirstName);

        binder.forField(lastName)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct second name")
                .asRequired()
                .bind(Client::getLastName, Client::setLastName);

        binder.forField(email)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct email")
                .asRequired()
                .bind(Client::getEmail, Client::setEmail);

        binder.forField(passportNumber)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct number of passport")
                .asRequired()
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        binder.forField(phoneNumber)
                .withValidator(s -> s != null && !s.isEmpty(), "Please, write correct phone number")
                .asRequired()
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        cancel.addClickListener(e -> changeHandler.onChange());
        save.getElement().getThemeList().add("primary");
        save.addClickListener(e -> save());
    }

    public void editClient(Client newClient) {
        if(newClient == null){
            changeHandler.onChange();
            return;
        }
        if(newClient.getId() != null){
            client = clientRepo.findById(newClient.getId()).orElse(newClient);
        }else {
            client = newClient;
        }
        binder.setBean(client);
    }

    private void save() {
        if (binder.validate().isOk()){
            clientRepo.save(client);
            changeHandler.onChange();
        }
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

}
