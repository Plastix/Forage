package io.github.plastix.forage.ui.compass;

import android.location.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.RxSchedulersOverrideRule;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import rx.Observable;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class CompassPresenterTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    private CompassPresenter compassPresenter;
    private CompassView view;
    private LocationInteractor locationInteractor;
    private AzimuthInteractor azimuthInteractor;

    @Before
    public void beforeEachTest() {
        view = mock(CompassView.class);
        locationInteractor = mock(LocationInteractor.class);
        azimuthInteractor = mock(AzimuthInteractor.class);
        compassPresenter = new CompassPresenter(azimuthInteractor, locationInteractor);
        compassPresenter.setView(view);
    }

    @Test
    public void updateCompass_updatesView() {
        Location target = new Location("");
        target.setLatitude(0);
        target.setLongitude(0);

        compassPresenter.setTargetLocation(target);
        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.just(0f));

        Location location = new Location("");
        location.setLatitude(0);
        location.setLongitude(0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location));

        compassPresenter.updateCompass();

        verify(view, times(1)).updateDistance(anyFloat());
        verify(view, times(1)).updateAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());
        verifyNoMoreInteractions(view);
    }
}
