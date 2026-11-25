
package com.snhu.mscs.cs600.sunfeature.data;

// Author: Tyler Daniels
// CS 600 Module Seven - Sunrise/Sunset external API integration.

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class SunriseSunsetService {

    public record SunData(
            String date,
            String sunrise,
            String sunset,
            String solarNoon,
            String dayLength,
            String timezone
    ) { }

    public static class IntegrationException extends RuntimeException {

        public IntegrationException(String message) {
            super(message);
        }

        public IntegrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String apiUrl;

    // Spring Boot constructor for dependency injection.
    @Autowired
    public SunriseSunsetService(
            @Value("${sunrise.api.url:https://api.sunrisesunset.io/json}")
            String apiUrl) {

        this(
                HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .build(),
                new ObjectMapper(),
                apiUrl
        );
    }

    // Constructor used by automated unit tests.
    SunriseSunsetService(
            HttpClient client,
            ObjectMapper mapper,
            String apiUrl) {

        this.client = client;
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    public SunData getSunData(double latitude, double longitude) {

        // Validate location coordinates.
        if (!Double.isFinite(latitude)
                || latitude < -90
                || latitude > 90
                || !Double.isFinite(longitude)
                || longitude < -180
                || longitude > 180) {

            throw new IntegrationException(
                    "Invalid coordinates supplied.");
        }

        URI uri;

        try {
            String separator = apiUrl.contains("?") ? "&" : "?";

            uri = URI.create(
                    apiUrl + separator
                            + "lat=" + latitude
                            + "&lng=" + longitude
            );

            // Validate the API URL.
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    && !"http".equalsIgnoreCase(uri.getScheme())) {

                throw new IllegalArgumentException(
                        "Unsupported URL scheme");
            }

        } catch (IllegalArgumentException ex) {

            throw new IntegrationException(
                    "The API URL is not configured correctly.", ex);
        }

        // Build the HTTP GET request.
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(8))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            // Send the request to the external API.
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // Handle HTTP errors.
            if (response.statusCode() != 200) {

                throw new IntegrationException(
                        "Sunrise/Sunset service returned HTTP "
                                + response.statusCode() + ".");
            }

            // Parse JSON response.
            JsonNode root = mapper.readTree(response.body());

            // Validate API response status.
            if (!"OK".equalsIgnoreCase(
                    root.path("status").asText())) {

                throw new IntegrationException(
                        "The API did not return a successful status.");
            }

            JsonNode results = root.path("results");

            if (!results.isObject()) {

                throw new IntegrationException(
                        "The API response is missing results.");
            }

            // Return validated data to the application.
            return new SunData(
                    required(results, "date"),
                    required(results, "sunrise"),
                    required(results, "sunset"),
                    required(results, "solar_noon"),
                    required(results, "day_length"),
                    required(results, "timezone")
            );

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new IntegrationException(
                    "The request was interrupted.", ex);

        } catch (IOException ex) {

            throw new IntegrationException(
                    "Unable to connect to Sunrise/Sunset. "
                            + "Check your internet connection.", ex);
        }
    }

    // Ensure that required API fields are present.
    private static String required(
            JsonNode results,
            String key) {

        JsonNode value = results.path(key);

        if (!value.isTextual() || value.asText().isBlank()) {

            throw new IntegrationException(
                    "The API response is missing: " + key);
        }

        return value.asText();
    }
}
