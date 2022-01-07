package app.model;

import java.util.TreeSet;
import java.util.Collections;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import org.hibernate.annotations.SortNatural;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import app.error.AuthenticationError;
import app.error.MissingError;
import app.model.generic.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Configurable(preConstruction = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class AccountModel extends BaseModel {
@Id @Setter @Getter @EqualsAndHashCode.Include @JsonProperty private String mail;
private String password;
@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @SortNatural private SortedSet<LocationModel> locations = new TreeSet<>();
@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @SortNatural private SortedSet<LocationModel> history = new TreeSet<>();
@Autowired @Transient private PasswordEncryptor passwordEncryptor;
@Setter @Getter @Transient private boolean Transient = false;

public AccountModel() {
    enableAllServices();
}

public void encryptAndSetPassword(String password) {
    this.password = passwordEncryptor.encryptPassword(password);
}

public void validatePassword(String password) {
    if (!passwordEncryptor.checkPassword(password, this.password))
        throw new AuthenticationError();
}

public SortedSet<LocationModel> getLocations() {
    return Collections.unmodifiableSortedSet(locations);
}

public SortedSet<LocationModel> getHistory() {
    return Collections.unmodifiableSortedSet(history);
}

public void addLocation(LocationModel location) {
    location.setAccount(this);
    location.setServices(getServices());
    locations.add(location);
}

protected LocationModel getLocationFrom(SortedSet<LocationModel> locations, String coords) {
    return locations.stream().filter(loc -> loc.getCoords().equals(coords)).findFirst().orElseThrow(MissingError::new);
}

public LocationModel getLocation(String coords) {
    return getLocationFrom(locations, coords);
}

public void removeLocation(String coords) {
    var location = getLocation(coords);
    locations.remove(location);
    history.add(location);
}

public LocationModel getHistoryLocation(String coords) {
    return getLocationFrom(history, coords);
}

public void restoreHistoryLocation(String coords) {
    var location = getHistoryLocation(coords);
    removeHistoryLocation(coords);
    addLocation(location);
}

public void removeHistoryLocation(String coords) {
    var location = getHistoryLocation(coords);
    history.remove(location);
}
}
