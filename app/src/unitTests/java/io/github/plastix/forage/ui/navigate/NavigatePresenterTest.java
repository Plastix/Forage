package io.github.plastix.forage.ui.navigate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NavigatePresenterTest {

    private NavigatePresenter navigatePresenter;

    @Mock
    private NavigateView navigateView;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        navigatePresenter = new NavigatePresenter();
        navigatePresenter.onViewAttached(navigateView);
    }

    @Test
    public void navigate_invalidLatitudeShouldOnlyCallErrorLatitude() {
        String lat = "latitude";
        String lon = "0";
        navigatePresenter.navigate(lat, lon);

        verify(navigateView, times(1)).errorParsingLatitude();
        verify(navigateView, never()).errorParsingLongitude();
        verify(navigateView, never()).openCompassScreen(anyDouble(), anyDouble());
    }

    @Test
    public void navigate_invalidLongitudeShouldOnlyCallErrorLongitude() {
        String lat = "0";
        String lon = "longitude";
        navigatePresenter.navigate(lat, lon);

        verify(navigateView, never()).errorParsingLatitude();
        verify(navigateView, times(1)).errorParsingLongitude();
        verify(navigateView, never()).openCompassScreen(anyDouble(), anyDouble());
    }


    @Test
    public void navigate_invalidLocationShouldErrorBoth() {
        String lat = "latitude";
        String lon = "longitude";
        navigatePresenter.navigate(lat, lon);

        verify(navigateView, times(1)).errorParsingLatitude();
        verify(navigateView, times(1)).errorParsingLongitude();
        verify(navigateView, never()).openCompassScreen(anyDouble(), anyDouble());
    }

    @Test
    public void navigate_validDataShouldOpenCompassScreen() {
        String lat = "0";
        String lon = "0";
        navigatePresenter.navigate(lat, lon);

        verify(navigateView, never()).errorParsingLatitude();
        verify(navigateView, never()).errorParsingLongitude();
        verify(navigateView, times(1)).openCompassScreen(Double.valueOf(lat), Double.valueOf(lon));
    }

    @Test
    public void navigate_noViewAttached() {
        navigatePresenter.onViewDetached();
        String lat = "latitude";
        String lon = "longitude";
        navigatePresenter.navigate(lat, lon);

        verifyZeroInteractions(navigateView);
    }
}
