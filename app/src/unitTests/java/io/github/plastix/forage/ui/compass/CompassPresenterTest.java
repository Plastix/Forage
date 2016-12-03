package io.github.plastix.forage.ui.compass;

import android.location.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.location.LocationUnavailableException;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.data.sensor.SensorUnavailableException;
import io.github.plastix.forage.util.LocationUtils;
import io.github.plastix.rx1.RxSchedulerRule;
import rx.Completable;
import rx.Observable;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class CompassPresenterTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    private CompassPresenter compassPresenter;

    @Mock
    private CompassView view;

    @Mock
    private LocationInteractor locationInteractor;

    @Mock
    private AzimuthInteractor azimuthInteractor;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        compassPresenter = new CompassPresenter(azimuthInteractor, locationInteractor);
        compassPresenter.onViewAttached(view);
    }

    @Test
    public void startCompass_updatesView() {
        Location target = LocationUtils.buildLocation(0, 0);

        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.just(0f));

        Location location = LocationUtils.buildLocation(0, 0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location));

        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());

        compassPresenter.startCompass(target);

        verify(view, times(1)).showCompass();
        verify(view, times(1)).setDistance(anyFloat());
        verify(view, times(1)).setAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());
    }

    @Test
    public void startCompassTwice_updatesView() {
        Location target = LocationUtils.buildLocation(0, 0);

        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.just(0f));

        Location location = LocationUtils.buildLocation(0, 0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location));

        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());

        compassPresenter.startCompass(target);

        verify(view, times(1)).showCompass();
        verify(view, times(1)).setDistance(anyFloat());
        verify(view, times(1)).setAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());

        compassPresenter.onViewDetached();
        compassPresenter.onViewAttached(view);
        compassPresenter.startCompass(target);

        verify(view, times(2)).showCompass();
    }

    @Test
    public void startCompass_updatesViewWithoutCompassSensor() {
        Location target = LocationUtils.buildLocation(0, 0);

        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.error(new SensorUnavailableException()));

        Location location = LocationUtils.buildLocation(0, 0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location));

        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());

        compassPresenter.startCompass(target);

        verify(view, times(1)).showCompass();
        verify(view, times(1)).setDistance(anyFloat());
        verify(view, times(1)).setAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());

    }

    @Test
    public void startCompass_opensDialogWhenNoLocation() {
        when(locationInteractor.isLocationAvailable())
                .thenReturn(Completable.error(new LocationUnavailableException()));

        compassPresenter.startCompass(mock(Location.class));

        verify(view, times(1)).showLocationUnavailableDialog();
    }
}
