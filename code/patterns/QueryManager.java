package app.manager;

import java.util.List;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.api.query.CoordsQuery;
import app.api.query.generic.BaseQuery;
import app.error.MissingError;
import app.manager.generic.BaseManager;
import app.model.LocationModel;

@Service
public class QueryManager extends BaseManager {
@Autowired private SortedSet<BaseQuery> services;
@Autowired private CoordsQuery normalQuery;

public List<LocationModel> getAllData(String query) {
    return services.stream().parallel().flatMap(service -> service.getData(query).stream()).flatMap(location -> normalQuery.getData(location.getCoords()).stream()).distinct().toList();
}

public LocationModel getData(String query) {
    return getAllData(query).stream().findFirst().orElseThrow(MissingError::new);
}
}