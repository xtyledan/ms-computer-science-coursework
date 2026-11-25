
package com.snhu.mscs.cs600.sunfeature.ui;

// Author: Tyler Daniels
// CS 600 Module Seven - display third-party Sunrise/Sunset API data.

import com.snhu.mscs.cs600.base.ui.component.ViewToolbar;
import com.snhu.mscs.cs600.sunfeature.data.SunriseSunsetService;
import com.snhu.mscs.cs600.sunfeature.data.SunriseSunsetService.SunData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("sunrise-sunset")
@AnonymousAllowed
@Menu(order = 2, icon = "vaadin:sun-o", title = "Sunrise & Sunset")
public class SunriseSunsetView extends VerticalLayout {

    private static final double LATITUDE = 47.62061;
    private static final double LONGITUDE = -122.34926;

    private final SunriseSunsetService service;
    private final Paragraph status = new Paragraph(
            "Select Refresh Data to retrieve current information.");
    private final VerticalLayout details = new VerticalLayout();

    public SunriseSunsetView(SunriseSunsetService service) {
        this.service = service;

        setPadding(true);
        setSpacing(true);
        setMaxWidth("760px");

        Button refresh = new Button(
                "Refresh Data", event -> refreshData());

        Anchor attribution = new Anchor(
                "https://sunrisesunset.io/",
                "Data provided by SunriseSunset.io");
        attribution.setTarget("_blank");

        add(
                new ViewToolbar("Sunrise & Sunset"),
                new H2("Seattle Space Needle"),
                new Paragraph("Coordinates: 47.62061, -122.34926"),
                refresh,
                status,
                details,
                attribution
        );

        refreshData();
    }

    private void refreshData() {
        details.removeAll();
        status.setText("Retrieving Sunrise/Sunset data...");

        try {
            SunData data = service.getSunData(
                    LATITUDE, LONGITUDE);

            details.add(
                    new Paragraph("Date: " + data.date()),
                    new Paragraph("Sunrise: " + data.sunrise()),
                    new Paragraph("Sunset: " + data.sunset()),
                    new Paragraph("Solar noon: " + data.solarNoon()),
                    new Paragraph("Day length: " + data.dayLength()),
                    new Paragraph("Timezone: " + data.timezone())
            );

            status.setText(
                    "Success: API information retrieved and displayed.");

        } catch (SunriseSunsetService.IntegrationException ex) {
            status.setText(
                    "Unable to retrieve data: " + ex.getMessage());
        }
    }
}
