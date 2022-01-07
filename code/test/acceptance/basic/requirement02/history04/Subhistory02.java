package app.test.acceptance.basic.requirement02.history04;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.instanceOf;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información del clima sobre un una ubicación activa
public class Subhistory02 extends SessionTest {
    @Test
    public void valid1() {
        // Given
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);

        var name = "Castellon";
        client.location.addLocation(name);

        name = "Madrid";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data.temp"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.rain"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.wind"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.icon"), instanceOf(String.class));
        response.body(setupServiceQuery(type, "data.description"), instanceOf(String.class));
    }

    @Test
    public void valid2() {
        // Given
        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);

        var name = "Madrid";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        var alias = "Antarctica";
        coords = "-78.159,16.406";
        location = client.location.addLocation(coords, alias);
        coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data.temp"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.rain"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.wind"), instanceOf(Number.class));
        response.body(setupServiceQuery(type, "data.icon"), instanceOf(String.class));
        response.body(setupServiceQuery(type, "data.description"), instanceOf(String.class));
    }
}
