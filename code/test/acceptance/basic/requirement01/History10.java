package app.test.acceptance.basic.requirement01;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero dar de baja una ubicación disponible, con el fin de eliminar información que ya no resulta de interés.
public class History10 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon de la Plana";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.removeLocation(coords);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("", hasSize(0));
        stateHistory.body("", hasSize(0));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon de la Plana";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);
        coords = "39.503,-0.405";
        Mockito.reset(spy.accountManager);

        // When
        var response = client.history.removeLocation(coords);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.NOT_FOUND.value());
        var statePlaces = client.location.getLocations();
        var stateHistory = client.history.getLocations();
        statePlaces.body("", hasSize(0));
        stateHistory.body("", hasSize(1));
        stateHistory.body("name", hasItem(name));
    }
}
