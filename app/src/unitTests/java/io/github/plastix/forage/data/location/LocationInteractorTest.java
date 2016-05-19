package io.github.plastix.forage.data.location;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import rx.Completable;
import rx.Observable;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class LocationInteractorTest {

    private LocationObservableFactory locationObservableFactory;
    private LocationCompletableFactory locationCompletableFactory;
    private LocationInteractor locationInteractor;

    @Before
    public void beforeEachTest() {
        locationObservableFactory = mock(LocationObservableFactory.class);
        locationCompletableFactory = mock(LocationCompletableFactory.class);

        locationInteractor = new LocationInteractor(locationObservableFactory, locationCompletableFactory);
    }

    @Test
    public void isLocationAvailable_returnsLocationCompletable() {
        Completable result = Completable.complete();
        when(locationCompletableFactory.buildLocationCompletable()).thenReturn(result);

        Completable out = locationInteractor.isLocationAvailable();

        verify(locationCompletableFactory, times(1)).buildLocationCompletable();
        assertThat(out).isEqualTo(result);
    }

    @Test
    public void getUpdatedLocation_returnsLocationObservable() {
        Observable<Location> result = Observable.empty();
        when(locationObservableFactory.buildObservable(any(LocationRequest.class))).thenReturn(result);

        locationInteractor.getUpdatedLocation();

        verify(locationObservableFactory, times(1)).buildObservable(any(LocationRequest.class));
    }

    @Test
    public void getLocationObservable_returnsLocationObservable() {
        Observable<Location> result = Observable.empty();
        when(locationObservableFactory.buildObservable(any(LocationRequest.class))).thenReturn(result);

        locationInteractor.getLocationObservable(1000);

        verify(locationObservableFactory, times(1)).buildObservable(any(LocationRequest.class));
    }
}
