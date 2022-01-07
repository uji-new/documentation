package app.test.acceptance.advanced.requirement03;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import app.test.generic.SessionTest;

// Como usuario quiero recibir sugerencias de autocompletado correspondientes a lugares para evitar potenciales búsquedas de lugares inexistentes.
public class History01 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var namePartial = "cast";
        var name = "Castellon de la Plana";

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

        // When
        var response = client.query.query(name);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(0));
    }
}
