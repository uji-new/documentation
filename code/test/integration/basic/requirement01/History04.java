package app.test.integration.basic.requirement01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.error.MissingError;
import app.model.LocationModel;
import app.test.generic.SessionTest;

// Como usuario quiero validar las coordenadas geográficas de una ubicación disponible en los servicios API activos, con el fin de evaluar su utilidad.
public class History04 extends SessionTest {
    @Test
    public void valid() {
        // Given
        var name = "Valencia";
        var locationMock = new LocationModel(name, 39.503, -0.405);
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(name);
        client.location.addLocation(name);

        name = "NAME";
        locationMock = new LocationModel(name, 39.980, -0.033);
        var coords = locationMock.getCoords();
        Mockito.doReturn(locationMock).when(spy.queryManager).getData(coords);
        Mockito.doReturn(true).when(spy.weatherService).getData(locationMock);

        var type = ServiceType.WEATHER.name();
        client.service.enableService(type);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.OK.value());
        response.body(setupEnabledQuery(true, ""), hasSize(1));
        response.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid() {
        // Given
        var coords = "180,360";
        Mockito.doThrow(new MissingError()).when(spy.queryManager).getData(coords);

        // When
        var response = client.service.getServicesForLocation(coords);

        // Then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
