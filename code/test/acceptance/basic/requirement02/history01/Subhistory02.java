package app.test.acceptance.basic.requirement02.history01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero consultar información de hasta tres servicios de una ubicación simultáneamente, con el fin de estar al corriente de novedades en todas ellas.
public class Subhistory02 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var typeA = ServiceType.WEATHER.name();
        client.service.enableService(typeA);

        var typeB = ServiceType.EVENTS.name();
        client.service.enableService(typeB);

        var name = "Castellon";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(true, ""), hasSize(2));
        response.body(setupEnabledQuery(true, "service.type"), hasItems(typeA, typeB));
    }

    @Test
    public void invalid() {
        // Given
        // No services
        // No locations
        var name = "INVALIDO";

        // When
        var response = client.service.getServicesForLocation(name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
