package app.test.acceptance.basic.requirement02.history04;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;

import static org.hamcrest.Matchers.instanceOf;

import static org.hamcrest.Matchers.everyItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.SessionTest;

// Como usuario quiero consultar fácilmente la información de noticias sobre un una ubicación activa.
public class Subhistory04 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var type = ServiceType.NEWS.name();
        client.service.enableService(type);

        var name = "Castellon";
        client.location.addLocation(name);

        name = "Madrid";
        var location = client.location.addLocation(name);
        var coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data"), hasSize(greaterThan(0)));
        response.body(setupServiceQuery(type, "data.title"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.description"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.url"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.author"), everyItem(instanceOf(String.class)));
        response.body(setupServiceQuery(type, "data.image"), everyItem(anyOf(instanceOf(String.class), equalTo(false))));
    }

    @Test
    public void invalid() {
        // Given
        var type = ServiceType.NEWS.name();
        client.service.enableService(type);

        var name = "Madrid";
        client.location.addLocation(name);

        var alias = "Antarctica";
        var coords = "-78.159,16.406";
        var location = client.location.addLocation(coords, alias);
        coords = location.extract().jsonPath().getString("coords");

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body("", hasSize(1));
        response.body(setupServiceQuery(type, "data"), hasSize(0));
    }
}
