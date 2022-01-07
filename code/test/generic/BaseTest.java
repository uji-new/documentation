package app.test.generic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import app.client.generic.Client;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {
    @Autowired protected Spy spy;
    @Autowired protected Client client;
    @LocalServerPort private int port;

    protected String getId(TestInfo info) {
        var history = info.getTestClass().orElseThrow().getName();
        var test = info.getTestMethod().orElseThrow().getName();
        return String.format("%s.%s", history, test);
    }

    private String setupQuery(String filter, String path) {
        return String.format("%s.%s", filter, path);
    }

    protected String setupEnabledQuery(boolean enabled, String path) {
        var filter = String.format("findAll{it.enabled==%b}", enabled);
        return setupQuery(filter, path);
    }

    protected String setupCoordsQuery(String coords, String path) {
        var filter = String.format("find{it.coords=='%s'}", coords);
        return setupQuery(filter, path);
    }

    protected String setupServiceQuery(String type, String path) {
        var filter = String.format("find{it.service.type=='%s'}", type);
        return setupQuery(filter, path);
    }

    @BeforeEach
    public void beforeEach(TestInfo info) {
        client.setPort(port);
        client.setupSession();
    }

    @AfterEach
    public void afterEach(TestInfo info) {
    }
}
