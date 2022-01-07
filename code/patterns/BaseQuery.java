package app.api.query.generic;

import java.util.List;

import app.api.generic.BaseApi;
import app.model.LocationModel;

public abstract class BaseQuery extends BaseApi<QueryType, String, List<LocationModel>> {
protected LocationModel newLocation(String name, double latitude, double longitude) {
    return new LocationModel(name, latitude, longitude);
}
}
