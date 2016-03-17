package io.github.plastix.forage.data.local.model;

import android.location.Location;

import io.realm.RealmObject;

/**
 * Realm Model "wrapper" to store location data in a Realm. This is required because Realm doesn't
 * allow third party types such as Android's location type.
 */
public class RealmLocation extends RealmObject {

    public double latitude;
    public double longitude;

    /**
     * Returns a new Android Location object with the corresponding data from the realm object.
     *
     * @return New Location object.
     */
    public Location toLocation() {
        Location location = new Location("Realm");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
