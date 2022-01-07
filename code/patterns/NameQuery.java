package app.api.query;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.jcabi.aspects.Cacheable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import app.api.query.generic.BaseQuery;
import app.model.LocationModel;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

@Service
@ConfigurationProperties("app.api.travelpayouts")
public class NameQuery extends BaseQuery {
@Override
protected RequestSpecification setupRequest(String info) {
    return super.setupRequest(info).queryParam("term", info);
}

protected String getLocalQuery(String path) {
    var country = getQuery().get("locale");
    return String.format("findAll{it.country_code == '%s'}.%s", country, path);
}

@Override
protected List<LocationModel> extractData(JsonPath body) {
    List<String> nameList = body.getList(getLocalQuery("name"));
    List<Map<String, Number>> coordsList = body.getList(getLocalQuery("coordinates"));
    return IntStream.range(0, nameList.size()).mapToObj(i -> {
        var name = nameList.get(i);
        var coords = coordsList.get(i);
        var latitude = coords.get("lat").doubleValue();
        var longitude = coords.get("lon").doubleValue();
        return newLocation(name, latitude, longitude);
    }).toList();
}

@Cacheable(forever = true)
public List<LocationModel> getData(String info) {
    return super.getData(info);
}
}