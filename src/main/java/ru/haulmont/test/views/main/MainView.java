package ru.haulmont.test.views.main;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import ru.haulmont.test.views.clients.ClientsView;
import ru.haulmont.test.views.banks.BanksView;
import ru.haulmont.test.views.loans.LoansView;
import ru.haulmont.test.views.loanoffers.LoanOffersView;


/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "MyBank", shortName = "MyBank", enableInstallPrompt = false)
@Theme(themeFolder = "mybank")
public class MainView extends AppLayout {

    private final Tabs menu;

    public MainView() {
        HorizontalLayout header = createHeader();
        menu = createMenuTabs();
        addToNavbar(createTopBar(header, menu));
    }

    private HorizontalLayout createTopBar(HorizontalLayout header, Tabs menu) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.getThemeList().add("dark");
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.setPadding(false);
        menu.setId("menu");
        header.add(menu);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(header);
        return layout;
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setId("header");
        Image logo = new Image("images/logo.png", "MyBank logo");
        logo.setId("logo");
        header.add(logo);
        header.add(new H1("MyBanks"));
        return header;
    }

    private static Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.getStyle().set("max-width", "100%");
        tabs.add(getAvailableTabs());
        return tabs;
    }

    private static Tab[] getAvailableTabs() {
        return new Tab[]{createTab("Clients", ClientsView.class), createTab("Banks", BanksView.class),
                createTab("Loans", LoansView.class), createTab("Loan Offers", LoanOffersView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }
}
