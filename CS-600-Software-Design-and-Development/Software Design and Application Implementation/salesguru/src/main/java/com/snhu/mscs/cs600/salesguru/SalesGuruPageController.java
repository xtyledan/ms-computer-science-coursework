package com.snhu.mscs.cs600.salesguru;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SalesGuruPageController {

    private final SalesGuruService contacts;
    private final ItemService items;
    private final InvoiceService invoices;

    public SalesGuruPageController(
            SalesGuruService contacts,
            ItemService items,
            InvoiceService invoices) {

        this.contacts = contacts;
        this.items = items;
        this.invoices = invoices;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/invoices";
    }

    @GetMapping("/companies")
    public String companies(Model model) {

        model.addAttribute(
            "companies",
            contacts.listCompanies()
        );

        return "companies";
    }

    @GetMapping("/items")
    public String items(Model model) {

        model.addAttribute("items", items.findAll());

        return "items";
    }

    @PostMapping("/items")
    public String addItem(
            @RequestParam String description,
            @RequestParam BigDecimal cost,
            @RequestParam BigDecimal retail,
            RedirectAttributes redirect) {

        try {

            items.create(description, cost, retail);

            redirect.addFlashAttribute(
                "success",
                "Product created."
            );

        } catch (IllegalArgumentException ex) {

            redirect.addFlashAttribute(
                "error",
                ex.getMessage()
            );
        }

        return "redirect:/items";
    }

    @GetMapping("/invoices")
    public String invoices(Model model) {

        model.addAttribute(
            "invoices",
            invoices.findAll()
        );

        return "invoices";
    }

    @GetMapping("/invoices/new")
    public String newInvoice(Model model) {

        model.addAttribute(
            "companies",
            contacts.listCompanies()
        );

        model.addAttribute(
            "representatives",
            contacts.listRepresentatives()
        );

        model.addAttribute(
            "items",
            items.findAll()
        );

        model.addAttribute("today", LocalDate.now());

        return "invoice-form";
    }

    @PostMapping("/invoices")
    public String addInvoice(
            @RequestParam Integer customerNumber,
            @RequestParam Integer representativeId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateOfSale,
            @RequestParam("itemNumber")
            List<Integer> itemNumbers,
            @RequestParam("quantity")
            List<Integer> quantities,
            @RequestParam("discountPercent")
            List<BigDecimal> discounts,
            RedirectAttributes redirect) {

        try {

            if (itemNumbers.size() != quantities.size() ||
                itemNumbers.size() != discounts.size()) {

                throw new IllegalArgumentException(
                    "Invoice item fields are incomplete.");
            }

            List<InvoiceData.NewLine> entries =
                new ArrayList<>();

            for (int i = 0; i < itemNumbers.size(); i++) {

                entries.add(new InvoiceData.NewLine(
                    itemNumbers.get(i),
                    quantities.get(i),
                    discounts.get(i)
                ));
            }

            InvoiceData.InvoiceView saved =
                invoices.create(
                    new InvoiceData.NewInvoice(
                        customerNumber,
                        representativeId,
                        dateOfSale,
                        entries
                    )
                );

            redirect.addFlashAttribute(
                "success",
                "Invoice created."
            );

            return "redirect:/invoices/" +
                saved.invoiceNumber();

        } catch (
            IllegalArgumentException |
            NoSuchElementException ex
        ) {

            redirect.addFlashAttribute(
                "error",
                ex.getMessage()
            );

            return "redirect:/invoices/new";
        }
    }

    @GetMapping("/invoices/{id}")
    public String invoiceDetails(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute(
            "invoice",
            invoices.get(id)
        );

        return "invoice-details";
    }
}