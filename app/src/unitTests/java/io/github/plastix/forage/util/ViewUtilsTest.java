package io.github.plastix.forage.util;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ViewUtilsTest {

    @Mock
    View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void show_callsCorrectMethod() {
        ViewUtils.show(view);

        verify(view).setVisibility(View.VISIBLE);
    }

    @Test
    public void gone_callsCorrectMethod() {
        ViewUtils.hide(view);

        verify(view).setVisibility(View.GONE);
    }

    @Test
    public void invis_callsCorrectMethod() {
        ViewUtils.invis(view);

        verify(view).setVisibility(View.INVISIBLE);
    }
}
