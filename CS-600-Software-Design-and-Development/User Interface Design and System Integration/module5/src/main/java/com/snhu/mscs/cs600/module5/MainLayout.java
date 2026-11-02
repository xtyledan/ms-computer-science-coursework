package com.snhu.mscs.cs600.module5;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/**
 * Student: Tyler Daniels
 *
 * Provides the application header and drawer-based side navigation.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createNavigation();
    }

    /**
     * Displays the logo, application name, student name, and drawer toggle.
     */
    private void createHeader() {
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.setAriaLabel("Open or close navigation");

        Image logo = new Image("images/module5-logo.svg", "Module5 book logo");
        logo.setWidth("40px");
        logo.setHeight("40px");

        H1 applicationName = new H1("Module5");
        applicationName.getStyle()
                .set("font-size", "1.5rem")
                .set("margin", "0");

        Span studentName = new Span("Tyler Daniels");

        HorizontalLayout header = new HorizontalLayout(
                drawerToggle,
                logo,
                applicationName,
                studentName
        );

        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1, applicationName);
        header.getStyle().set("padding-right", "1rem");

        addToNavbar(header);
    }

    /**
     * Places the Books List navigation item inside the application drawer.
     */
    private void createNavigation() {
        SideNav navigation = new SideNav();

        navigation.addItem(new SideNavItem("Books List", BooksView.class));
        navigation.getStyle().set("margin", "1rem");

        addToDrawer(navigation);
    }
}