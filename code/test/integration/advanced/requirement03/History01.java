package app.test.integration.advanced.requirement03;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero recibir sugerencias de autocompletado correspondientes a lugares para evitar potenciales búsquedas de lugares inexistentes.
public class History01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var namePartial = "cast";
        var name = "Castellon";
        var locationMock = new LocationModel(name, 39.980, -0.033);
        Mockito.doReturn(List.of(locationMock)).when(spy.queryManager).getAllData(namePartial);

        // When
        var response = client.query.query(namePartial);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body("name", hasItem(name));
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
