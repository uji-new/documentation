package app.test.integration.basic.requirement04;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import app.test.generic.BaseTest;

// Como usuario quiero que cada vez que inicie la aplicación, sus contenidos y aspecto sean idénticos a los que había la última vez que se cerró, con el fin de evitar reconfigurarla en cada uso.
public class History01 extends BaseTest {
    @Test
    public void valid1(TestInfo info) {
        // Given
        // No account
        var id = getId(info);
        Mockito.reset(spy.accountManager);

        // When
        var response = client.account.register(id, id);

        // Then
        Mockito.verify(spy.accountManager).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
    }

    @Test
    public void valid2(TestInfo info) {
        // Given
        var id = getId(info);
        client.account.register(id, id);
        client.session.logout();
        Mockito.reset(spy.accountManager);

        // When
        var response = client.session.login(id, id);

        // Then
        Mockito.verify(spy.accountManager, never()).saveAccount(any());
        response.statusCode(HttpStatus.OK.value());
    }
}
