package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero dar de alta una ubicación a partir de unas coordenadas geográficas, con el fin de tenerla disponible en el sistema.
public class History02 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var coords = "39.978,-0.033";
        Mockito.reset(spy.accountManager);

        // When
        var response = client.location.addLocation(coords);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.location.getLocations();
        state.body("", hasSize(1));
        state.body("coords", hasItem(coords));
    }

    @Test
    public void invalid() {
        // Given
        var coords = "180.0,360.0";
        Mockito.reset(spy.accountManager);

        // When
        var response = client.location.addLocation(coords);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.location.getLocations();
        state.body("", hasSize(0));
    }
}
