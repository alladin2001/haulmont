package ru.haulmont.test.views.loanoffers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import ru.haulmont.test.data.entity.LoanOffer;
import ru.haulmont.test.data.repos.LoanOfferRepo;
import ru.haulmont.test.views.main.MainView;

@Route(value = "loan-offers", layout = MainView.class)
@PageTitle("Loan offers")
public class LoanOffersView extends Div {

    private final ScheduleDialog scheduleDialog;
    private final LoanOfferRepo loanOfferRepo;
    private Grid<LoanOffer> loanOfferGrid = new Grid<>(LoanOffer.class, false);

    @Autowired
    public LoanOffersView(ScheduleDialog scheduleDialog, LoanOfferRepo loanOfferRepo) {
        addClassName("loan-offers-view");
        this.scheduleDialog = scheduleDialog;
        this.loanOfferRepo = loanOfferRepo;
        loanOfferGrid.addColumn(e -> String.valueOf(e.getClient().getFirstName() + " " + e.getClient().getLastName()))
                .setHeader("Client").setSortable(true);
        loanOfferGrid.addColumn(e -> e.getLoan().getName()).setHeader("Loan").setSortable(true);
        loanOfferGrid.addColumn(LoanOffer::getSumLoan).setHeader("Sum of loan").setSortable(true);

        loanOfferGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        loanOfferGrid.removeThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        loanOfferGrid.setItems(loanOfferRepo.findAll());
        HorizontalLayout layout = new HorizontalLayout(loanOfferGrid);
        loanOfferGrid.setHeight("900px");
        layout.setHeight("900px");
        add(layout);
        loanOfferGrid.addItemDoubleClickListener(e ->  scheduleDialog.open());
    }

}
