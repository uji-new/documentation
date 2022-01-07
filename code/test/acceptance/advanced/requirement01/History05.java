package app.test.acceptance.advanced.requirement01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero poder cerrar sesión con unas credenciales únicas para que no me identifique temporalmente la aplicación.
public class History05 extends BaseTest {
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
        client.account.register(id, id);

        // When
        var response = client.session.logout();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.session.getAccount();
        state.statusCode(HttpStatus.UNAUTHORIZED.value());
        state = client.session.login(id, id);
        response.statusCode(HttpStatus.OK.value());
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        client.session.logout();

        // When
        var response = client.session.logout();

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.session.getAccount();
        state.statusCode(HttpStatus.UNAUTHORIZED.value());
        state = client.session.login(id, id);
        response.statusCode(HttpStatus.OK.value());
    }
}
