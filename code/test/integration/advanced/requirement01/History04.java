package app.test.integration.advanced.requirement01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero poder iniciar sesión con unas credenciales únicas para que se me identifique temporalmente en la aplicación.
public class History04 extends BaseTest {
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
        client.session.logout();

        // When
        var response = client.session.login(id, id);

        // Then
        response.statusCode(HttpStatus.OK.value());
        var state = client.session.getAccount();
        state.statusCode(HttpStatus.OK.value());
    }

    @Test
    public void invalid(TestInfo info) {
        // Given
        var id = getId(info);
        var idNew = id + "Nuevo";
        client.account.register(id, id);
        client.session.logout();

        // When
        var response = client.session.login(idNew, idNew);

        // Then
        response.statusCode(HttpStatus.UNAUTHORIZED.value());
        var state = client.session.getAccount();
        state.statusCode(HttpStatus.UNAUTHORIZED.value());
        state = client.session.login(id, id);
        state.statusCode(HttpStatus.OK.value());
    }
}
