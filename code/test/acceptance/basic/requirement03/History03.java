package app.test.acceptance.basic.requirement03;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero conocer una breve descripción de cada fuente de información disponible (e.g. perfil de información, frecuencia de actualización, etc.), para poder tomar decisiones fundamentadas.
public class History03 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.WEATHER.name();

        // When
        var response = client.service.getServices();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupServiceQuery(type, "service"), equalTo(Map.of("type", type, "name", spy.weatherService.getName(), "description", spy.weatherService.getDescription())));
    }

    @Test
    public void invalid() {
        // Given
        var type = "INVALIDO";

        // When
        var response = client.service.getServices();

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupServiceQuery(type, "service"), nullValue());
    }
}
