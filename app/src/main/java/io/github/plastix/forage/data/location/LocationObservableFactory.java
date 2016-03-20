package io.github.plastix.forage.data.location;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Observable;

public class LocationObservableFactory {

    private Provider<LocationOnSubscribe> provider;

    @Inject
    public LocationObservableFactory(@NonNull Provider<LocationOnSubscribe> provider) {
        this.provider = provider;
    }

    @NonNull
    public Observable<Location> buildObservable(@NonNull LocationRequest locationRequest) {
        LocationOnSubscribe onSubscribe = provider.get();
        onSubscribe.setLocationRequest(locationRequest);
        return Observable.create(onSubscribe);
    }
}
