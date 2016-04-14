package io.github.plastix.forage.ui.compass;

import android.location.Location;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.RxSchedulersOverrideRule;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.util.RxUtils;
import rx.Observable;
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
//@PrepareForTest(
//        {
//                RxUtils.class,
//        }
//)
/**
 * Todo: Test {@link CompassPresenter#updateCompass()} and {@link CompassPresenter#onResume()}.
 */
@Ignore
public class CompassPresenterTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

//    @Rule
//    public PowerMockRule powerMockRule = new PowerMockRule();

    private CompassPresenter compassPresenter;
    private CompassView view;
    private LocationInteractor locationInteractor;
    private AzimuthInteractor azimuthInteractor;

    @Before
    public void beforeEachTest() {
        view = mock(CompassView.class);
        locationInteractor = mock(LocationInteractor.class);
        azimuthInteractor = mock(AzimuthInteractor.class);
        compassPresenter = spy(new CompassPresenter(azimuthInteractor, locationInteractor));
        compassPresenter.setView(view);

//        PowerMockito.spy(RxUtils.class);
    }

//    @Test
//    public void onStop_callsUnsubscribe() {
//        compassPresenter.onStop();
//        PowerMockito.verifyStatic();
//        RxUtils.safeUnsubscribe(any(Subscription.class));
//    }

    @Test
    public void updateCompass_updatesView() {
        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.just(0f));
        Location location = new Location("");
        location.setLatitude(0);
        location.setLongitude(0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location, location));

        compassPresenter.updateCompass();

        verify(view, times(1)).updateDistance(anyFloat());
        verify(view, times(1)).updateAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());
        verifyNoMoreInteractions();


    }
}
