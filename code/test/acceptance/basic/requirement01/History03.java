package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero validar el topónimo de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History03 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var type = ServiceType.WEATHER.name();
        client.location.addLocation(name);
        client.service.enableService(type);
        name = "Valencia";

        // When
        var response = client.service.getServicesForLocation(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(true, ""), hasSize(1));
        response.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";

        // When
        var response = client.service.getServicesForLocation(name);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
