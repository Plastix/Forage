package io.github.plastix.forage.ui.compass;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.Bind;
import icepick.State;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.PresenterFragment;
import io.github.plastix.forage.util.LocationUtils;

/**
 * Fragment that is responsible for the geocache compass.
 */
public class CompassFragment extends PresenterFragment<CompassPresenter> implements CompassView {

    private static final String EXTRA_CACHE_LOCATION = "CACHE_LOCATION";

    @Bind(R.id.compass_arrow)
    IconicsImageView arrow;

    @Bind(R.id.compass_distance)
    TextView distance;

    @State
    float currentAzimuth;

    @State
    Location target;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String raw_location = getArguments().getString(EXTRA_CACHE_LOCATION);
        this.target = LocationUtils.stringToLocation(raw_location);

        injectDependencies();
        initializePresenter();

    }

    private void injectDependencies() {
        ForageApplication.getComponent(getContext())
                .plus(new CompassModule(this)).injectTo(this);
    }

    private void initializePresenter() {
        presenter.setTargetLocation(target);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_compass;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrow.setRotation(currentAzimuth);
    }

    @Override
    public void rotateCompass(final Float degrees) {
        // Rotate by negative degrees because Android rotates counter-clockwise
        arrow.animate()
                .rotation(-degrees)
                .setDuration(100)
                .setInterpolator(new LinearInterpolator())
                .start();

        currentAzimuth = -degrees;
    }

    public void updateDistance(double distance) {
        this.distance.setText(String.valueOf(distance));
    }
}
