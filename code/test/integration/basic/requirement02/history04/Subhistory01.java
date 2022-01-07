package app.test.integration.basic.requirement02.history04;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasSize;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de cualquiera de las ubicaciones activas por separado.
public class Subhistory01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.503, -0.405);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);

        name = "Castellon";
        locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(2));
        response.body(setupCoordsQuery(coords, ""), equalTo(Map.of("name", name, "alias", name, "coords", coords)));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.503, -0.405);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        name = "Castellon";
        locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        location = client.location.addLocation(name);
        coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        // When
        var response = client.location.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
