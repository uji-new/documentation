package app.test.integration.basic.requirement03;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero activar un servicio de información (API), entre aquellos disponibles.
public class History02 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.WEATHER.name();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.enableService(type);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(1));
        state.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var type = "INVALIDO";
        Mockito.reset(spy.accountManager);

        // When
        var response = client.service.enableService(type);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.BAD_REQUEST.value());
        var state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(0));
    }
}
