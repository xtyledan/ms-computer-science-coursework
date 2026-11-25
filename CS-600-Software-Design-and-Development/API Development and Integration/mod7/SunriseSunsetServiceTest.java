
package com.snhu.mscs.cs600.sunfeature.data;

// Author: Tyler Daniels
// CS 600 Module Seven - unit tests for integration and failure handling.

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SunriseSunsetServiceTest {

    private HttpClient client;
    private HttpResponse<String> response;
    private SunriseSunsetService service;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        client = mock(HttpClient.class);
        response = mock(HttpResponse.class);

        service = new SunriseSunsetService(
                client,
                new ObjectMapper(),
                "https://api.sunrisesunset.io/json");
    }

    @Test
    void successfulResponseReturnsSunData() throws Exception {
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("""
                {"status":"OK","results":{
                  "date":"2026-11-11",
                  "sunrise":"7:10:00 AM",
                  "sunset":"4:35:00 PM",
                  "solar_noon":"11:52:30 AM",
                  "day_length":"9:25:00",
                  "timezone":"America/Los_Angeles"
                }}
                """);

        when(client.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        var result = service.getSunData(
                47.62061, -122.34926);

        assertEquals("7:10:00 AM", result.sunrise());
        assertEquals("4:35:00 PM", result.sunset());
        assertEquals(
                "America/Los_Angeles", result.timezone());

        verify(client).send(
                argThat(request ->
                        request.uri().toString()
                                .contains("lat=47.62061")
                        && request.uri().toString()
                                .contains("lng=-122.34926")),
                any(HttpResponse.BodyHandler.class));
    }

    @Test
    void httpFailureShowsMeaningfulError() throws Exception {
        when(response.statusCode()).thenReturn(503);

        when(client.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        var error = assertThrows(
                SunriseSunsetService.IntegrationException.class,
                () -> service.getSunData(
                        47.62061, -122.34926));

        assertTrue(error.getMessage().contains("503"));
    }

    @Test
    void missingResponseFieldsAreRejected() throws Exception {
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(
                "{\"status\":\"OK\",\"results\":{\"sunrise\":\"7 AM\"}}");

        when(client.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        assertThrows(
                SunriseSunsetService.IntegrationException.class,
                () -> service.getSunData(
                        47.62061, -122.34926));
    }

    @Test
    void networkFailureIsHandled() throws Exception {
        when(client.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network unavailable"));

        var error = assertThrows(
                SunriseSunsetService.IntegrationException.class,
                () -> service.getSunData(
                        47.62061, -122.34926));

        assertTrue(error.getMessage()
                .contains("Unable to connect"));
    }

    @Test
    void invalidCoordinatesFailBeforeHttpCall() {
        assertThrows(
                SunriseSunsetService.IntegrationException.class,
                () -> service.getSunData(
                        100, -122.34926));

        verifyNoInteractions(client);
    }
}
