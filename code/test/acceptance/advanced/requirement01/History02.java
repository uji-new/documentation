package app.test.acceptance.advanced.requirement01;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero poder eliminar unas credenciales únicas para que ya no estén disponibles para iniciar esa sesión.
public class History02 extends BaseTest {
    @Test
    public void valid(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.deregister();

        // Then
        Mockito.verify(spy.accountManager).deleteAccount(any());
        response.statusCode(HttpStatus.OK.value());
        var state = client.session.login(id, id);
        state.statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        client.session.logout();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.deregister();

        // Then
        Mockito.verify(spy.accountManager, never()).deleteAccount(any());
        response.statusCode(HttpStatus.UNAUTHORIZED.value());
        var state = client.session.login(id, id);
        state.statusCode(HttpStatus.OK.value());
    }
}
