package ru.haulmont.test.views.loans;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import ru.haulmont.test.data.entity.Bank;
import ru.haulmont.test.data.entity.Loan;
import ru.haulmont.test.data.repos.LoanRepo;
import ru.haulmont.test.views.main.MainView;

@Route(value = "loans", layout = MainView.class)
@PageTitle("Loans")
public class LoansView extends Div {

    private LoanRepo loanRepo;
    private final LoanAddEditor loanAddEditor;

    private Button add = new Button("Add");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");

    private Grid<Loan> loanGrid = new Grid<>(Loan.class, false);

    public LoansView(LoanRepo loanRepo, LoanAddEditor loanAddEditor) {
        addClassName("loans-view");
        this.loanRepo = loanRepo;
        this.loanAddEditor = loanAddEditor;
        HorizontalLayout horizontalLayout = new HorizontalLayout(add, edit, delete);
        horizontalLayout.setSpacing(true);

        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        edit.setEnabled(false);

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.setEnabled(false);

        loanGrid.addColumn(Loan::getName).setHeader("Name of Loan").setKey("name").setSortable(true);
        loanGrid.addColumn(Loan::getMaxLimit).setSortable(true).setKey("limit").setHeader("Max limit");
        loanGrid.addColumn(Loan::getPercent).setSortable(true).setKey("percent").setHeader("Percent, %");
        loanGrid.addColumn(e -> e.getBank().getName()).setSortable(true).setKey("bank").setHeader("Bank");

        add(horizontalLayout);
        HorizontalLayout layout = new HorizontalLayout(loanGrid);
        loanGrid.setHeight("900px");
        layout.setHeight("900px");
        add(layout);
        show();
        loanGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        loanGrid.removeThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        createListeners();
    }

    private void createListeners() {
        loanAddEditor.setChangeHandler(() -> show());
        add.addClickListener(e -> addEditLoan(new Loan()));
        edit.addClickListener(e-> addEditLoan(loanGrid.asSingleSelect().getValue()));
        delete.addClickListener(e -> {loanRepo.delete(loanGrid.asSingleSelect().getValue());show();});


        loanGrid.addItemDoubleClickListener(e -> addEditLoan(e.getItem()));
        loanGrid.addItemClickListener(valueChangeEvent -> {
            if (!loanGrid.asSingleSelect().isEmpty()) {
                edit.setEnabled(true);
                delete.setEnabled(true);
            }
            else {
                edit.setEnabled(false);
                delete.setEnabled(false);
            }
        });
    }

    private void addEditLoan(Loan loan) {
        loanAddEditor.open();
        loanAddEditor.editClient(loan);
        show();
    }

    private void show(){
        loanGrid.setItems(loanRepo.findAll());
    }
}
