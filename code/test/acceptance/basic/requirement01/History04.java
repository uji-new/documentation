package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero validar las coordenadas geográficas de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History04 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Valencia";
        var coords = "39.978,-0.033";
        var type = ServiceType.WEATHER.name();
        client.location.addLocation(name);
        client.service.enableService(type);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(true, ""), hasSize(1));
        response.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var coords = "180,360";

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
