package app.test.acceptance.basic.requirement02;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero consultar el histórico de ubicaciones, con el fin de facilitar la reactivación de alguna en caso de necesidad.
public class History05 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        client.location.addLocation(name);

        name = "Valencia";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");
        client.location.removeLocation(coords);

        // When
        var response = client.history.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body("name", hasItem(name));
    }

    @Test
    public void invalid() {
        // Given
        var name = "Castellon";
        client.location.addLocation(name);

        name = "Valencia";
        client.location.addLocation(name);

        // When
        var response = client.history.getLocations();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
