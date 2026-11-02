package com.snhu.mscs.cs600.module5;

import java.util.Arrays;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Student: Tyler Daniels
 *
 * Displays the supplied TestData books in a scrollable, read-only grid.
 * Implements the Read operation using the existing sample data.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Books List | Module5")
public class BooksView extends VerticalLayout implements TestData {

    public BooksView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        H2 viewHeader = new H2("Books List");
        viewHeader.getStyle().set("margin", "0");

        Paragraph description = new Paragraph(
                "View the supplied books, authors, and publication years."
        );

        Grid<Book> booksGrid = new Grid<>(Book.class, false);

        booksGrid.addColumn(Book::getTitle)
                .setHeader("Title")
                .setAutoWidth(true)
                .setFlexGrow(2);

        booksGrid.addColumn(Book::getAuthor)
                .setHeader("Author")
                .setAutoWidth(true)
                .setFlexGrow(1);

        booksGrid.addColumn(Book::getYear)
                .setHeader("Publication Year")
                .setWidth("160px")
                .setFlexGrow(0);

        // Read the existing sample data without modifying the book records.
        booksGrid.setItems(Arrays.asList(book_list));

        // A bounded height enables scrolling through the grid's records.
        booksGrid.setWidthFull();
        booksGrid.setHeight("65vh");

        // No editors or mutation controls are configured.
        booksGrid.setSelectionMode(Grid.SelectionMode.NONE);

        Paragraph recordCount = new Paragraph(
                "Total books: " + book_list.length
        );

        add(viewHeader, description, booksGrid, recordCount);
    }
}