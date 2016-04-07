package io.github.plastix.forage.ui.compass;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.plastix.forage.RxSchedulersOverrideRule;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.util.RxUtils;
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
        {
                RxUtils.class,
        }
)
/**
 * Todo: Test {@link CompassPresenter#updateCompass()} and {@link CompassPresenter#onResume()}.
 */
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
        compassPresenter = spy(new CompassPresenter(azimuthInteractor, locationInteractor));
        compassPresenter.setView(view);

        PowerMockito.spy(RxUtils.class);
    }

    @Test
    public void onStop_callsUnsubscribe() {
        compassPresenter.onStop();
        PowerMockito.verifyStatic();
        RxUtils.safeUnsubscribe(any(Subscription.class));
    }
}
