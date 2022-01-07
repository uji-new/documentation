package app.test.integration.advanced.requirement02;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.BaseTest;

// Como usuario quiero dar de alta una ubicación a partir de un topónimo, con el fin de tenerla disponible en el sistema.
public class History01 extends BaseTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.session.loginAsGuest();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.location.addLocation(name);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.location.getLocations();
        state.body("", hasSize(1));
        state.body("name", hasItem(name));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(name);
        client.session.loginAsGuest();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.location.addLocation(name);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var state = client.location.getLocations();
        state.body("", hasSize(0));
    }
}
