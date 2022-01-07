package app.api.generic;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.InterruptedIOException;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcabi.aspects.Cacheable;

import org.springframework.beans.factory.annotation.Autowired;

import app.Configuration;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonAutoDetect(getterVisibility = Visibility.NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseApi<T extends BaseType<T>, I, O> implements Comparable<BaseApi<T, ?, ?>> {
    @JsonProperty @EqualsAndHashCode.Include @Getter private T type;
    @Getter(AccessLevel.PROTECTED) private String url;
    @Getter(AccessLevel.PROTECTED) private Map<String, String> query;
    @Autowired @Getter(AccessLevel.PROTECTED) private Configuration appConfiguration;

    public void setType(T type) {
        if (this.type != null)
            throw new IllegalAccessError();
        this.type = type;
    }

    public void setUrl(String url) {
        if (this.url != null)
            throw new IllegalAccessError();
        this.url = url;
    }

    public void setQuery(Map<String, String> query) {
        if (this.query != null)
            throw new IllegalAccessError();
        this.query = Collections.unmodifiableMap(query);
    }

    @Cacheable(forever = true)
    protected RestAssuredConfig getConfiguration() {
        // REST Assured's configuration uses CoreConnectionPNames, which is deprecated
        var timeout = (int) appConfiguration.getTimeout().toMillis();
        var params = Map.of("http.socket.timeout", timeout, "http.connection.timeout", timeout);
        var httpConfig = HttpClientConfig.httpClientConfig().addParams(params);
        return RestAssuredConfig.config().httpClient(httpConfig);
    }

    protected RequestSpecification setupRequest(I info) {
        return RestAssured.given().config(getConfiguration()).baseUri(url).queryParams(query);
    }

    protected ValidatableResponse validateResponse(ValidatableResponse response) {
        return response.statusCode(200);
    }

    protected Response processRequest(RequestSpecification request) {
        // REST Assured's request methods use Sneaky Throws, which can't be catched
        try {
            return request.get();
        } catch (Exception exception) {
            if (exception instanceof InterruptedIOException)
                fail(exception.getMessage());
            throw exception;
        }
    }

    protected abstract O extractData(JsonPath body);

public O getData(I info) {
    var request = setupRequest(info);
    var response = processRequest(request).then();
    var body = validateResponse(response).extract().jsonPath();
    return extractData(body);
}

    @Override
    public int compareTo(BaseApi<T, ?, ?> other) {
        return type.compareTo(other.type);
    }
}
