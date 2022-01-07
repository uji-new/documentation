package app.test.integration.basic.requirement03;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero consultar la lista de servicios de información disponibles (API), con el fin de elegir (activar) aquellos de interés.
public class History01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.WEATHER.name();
        client.service.enableAllServices();
        client.service.disableService(type);

        // When
        var response = client.service.getServices();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(false, ""), hasSize(1));
        response.body(setupEnabledQuery(false, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        client.service.enableAllServices();

        // When
        var response = client.service.getServices();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(false, ""), hasSize(0));
    }
}
