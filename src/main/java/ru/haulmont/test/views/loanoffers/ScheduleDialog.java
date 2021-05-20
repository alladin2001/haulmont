package ru.haulmont.test.views.loanoffers;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.LoanOffer;
import ru.haulmont.test.data.entity.LoanPayment;
import ru.haulmont.test.data.repos.LoanPaymentRepo;

@SpringComponent
@UIScope
public class ScheduleDialog extends Dialog {
    private final LoanPaymentRepo paymentRepo;


    private Grid<LoanPayment> loanPaymentGrid = new Grid<>(LoanPayment.class);

    @Autowired
    public ScheduleDialog(LoanPaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
        loanPaymentGrid.removeColumnByKey("id");
        loanPaymentGrid.removeColumnByKey("loanOffer");

        loanPaymentGrid.setItems(paymentRepo.findAll());
        setHeight("700px");
        setWidth("800px");
        add(loanPaymentGrid);
    }

    public void setSchedule(LoanOffer loanOffer){
        loanPaymentGrid.setItems(paymentRepo.findByLoanOffer(loanOffer));
    }
}
