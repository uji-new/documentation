package app.test.integration.basic.requirement02.history01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero consultar información de hasta tres ubicaciones simultáneamente, con el fin de saber todos sus datos a la vez.
public class Subhistory01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var nameA = "Castellon";
        var locationMockA = new LocationModel(nameA, 39.980, -0.033);
        Mockito.doReturn(locationMockA).when(spy.queryManager).getData(nameA);
        client.location.addLocation(nameA);

        var nameB = "Alicante";
        var locationMockB = new LocationModel(nameB, 38.53996, -0.50579);
        Mockito.doReturn(locationMockB).when(spy.queryManager).getData(nameB);
        client.location.addLocation(nameB);

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(2));
        response.body("name", hasItems(nameA, nameB));
    }

    @Test
    public void invalid() {
        // Given
        // No locations

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
