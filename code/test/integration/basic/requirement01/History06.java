package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.model.LocationModel;
import app.test.generic.SessionTest;

import java.util.Collections;
import java.util.List;

// Como usuario quiero obtener las coordenadas geográficas de una ubicación a partir de su topónimo, con el fin de facilitar la obtención de información en múltiples fuentes públicas (API).
public class History06 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var locationMock = new LocationModel(name, 39.980, -0.033);
        var coords = locationMock.getCoords();
        Mockito.doReturn(List.of(locationMock)).when(spy.queryManager).getAllData(name);

        // When
        var response = client.query.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body("coords", hasItem(coords));
    }

    @Test
    public void invalid() {
        // Given
        var name = "INVALIDO";
        Mockito.doReturn(Collections.emptyList()).when(spy.queryManager).getAllData(name);

        // When
        var response = client.query.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
