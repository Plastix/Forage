package io.github.plastix.forage.data.location;

import com.google.android.gms.location.LocationRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Provider;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class LocationInteractorTest {

    private Provider<LocationAsyncEmitter> locationAsyncEmitterProvider;
    private Provider<LocationAvailableAsyncEmitter> locationAvailableProvider;
    private LocationInteractor locationInteractor;
    private LocationAsyncEmitter locationAsyncEmitter;
    private LocationAvailableAsyncEmitter locationAvailableAsyncEmitter;

    @Before
    public void beforeEachTest() {
        locationAsyncEmitter = mock(LocationAsyncEmitter.class);
        locationAsyncEmitterProvider = () -> locationAsyncEmitter;

        locationAvailableAsyncEmitter = mock(LocationAvailableAsyncEmitter.class);
        locationAvailableProvider = () -> locationAvailableAsyncEmitter;

        locationInteractor = new LocationInteractor(locationAsyncEmitterProvider, locationAvailableProvider);
    }

    @Test
    public void getUpdatedLocation_returnsLocationSingle() {
        locationInteractor.getUpdatedLocation().toBlocking();

        verify(locationAsyncEmitter, times(1)).setLocationRequest(any(LocationRequest.class));
    }

    @Test
    public void getLocationObservable_returnsLocationObservable() {
        locationInteractor.getLocationObservable(1000).toBlocking();

        verify(locationAsyncEmitter, times(1)).setLocationRequest(any(LocationRequest.class));

    }
}
