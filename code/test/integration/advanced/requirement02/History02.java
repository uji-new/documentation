package app.test.integration.advanced.requirement02;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;

import org.springframework.http.HttpStatus;

import app.api.service.generic.ServiceType;
import app.test.generic.BaseTest;

// Como usuario quiero poder transformar una cuenta de invitado a una permanente para no necesitar recrear los ajustes de una en la otra.
public class History02 extends BaseTest {
    @Override
    @AfterEach
    public void afterEach(TestInfo info) {
        super.afterEach(info);
        client.account.deregister();
    }

    @Test
    public void valid(TestInfo info) {
        // Given
        var id = getId(info);
        var type = ServiceType.WEATHER.name();
        client.session.loginAsGuest();
        client.service.disableAllServices();
        client.service.enableService(type);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.register(id, id);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(1));
        state.body(setupEnabledQuery(true, "service.type"), hasItem(type));
        client.session.logout();

        state = client.session.login(id, id);
        state.statusCode(HttpStatus.OK.value());
        state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(1));
        state.body(setupEnabledQuery(true, "service.type"), hasItem(type));
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        var type = ServiceType.WEATHER.name();
        client.account.register(id, id);
        client.service.disableAllServices();
        client.session.logout();

        client.session.loginAsGuest();
        client.service.disableAllServices();
        client.service.enableService(type);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.register(id, id);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.CONFLICT.value());
        var state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(1));
        state.body(setupEnabledQuery(true, "service.type"), hasItem(type));
        client.session.logout();

        state = client.session.login(id, id);
        state.statusCode(HttpStatus.OK.value());
        state = client.service.getServices();
        state.body(setupEnabledQuery(true, ""), hasSize(0));
    }
}
