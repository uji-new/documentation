package app.test.generic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class SessionTest extends BaseTest {
    @Override
    @BeforeEach
    public void beforeEach(TestInfo info) {
        var id = getId(info);
        super.beforeEach(info);
        client.account.register(id, id);
        client.service.disableAllServices();
    }

    @Override
    @AfterEach
    public void afterEach(TestInfo info) {
        super.afterEach(info);
        client.account.deregister();
    }
}
