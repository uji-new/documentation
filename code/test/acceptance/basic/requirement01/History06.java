package app.test.acceptance.basic.requirement01;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.test.generic.SessionTest;

// Como usuario quiero obtener las coordenadas geográficas de una ubicación a partir de su topónimo, con el fin de facilitar la obtención de información en múltiples fuentes públicas (API).
public class History06 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Castellon";
        var coords = "39.970,-0.050";

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

        // When
        var response = client.query.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
