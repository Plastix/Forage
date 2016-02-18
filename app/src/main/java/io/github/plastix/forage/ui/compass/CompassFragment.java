package io.github.plastix.forage.ui.compass;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.PresenterFragment;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;

/**
 * Fragment that is responsible for the geocache compass.
 */
public class CompassFragment extends PresenterFragment<CompassPresenter> implements CompassView {

    private static final String EXTRA_CACHE_LOCATION = "CACHE_LOCATION";
    private static float CENTER = 0.5f;

    @Bind(R.id.compass_arrow)
    IconicsImageView arrow;

    @Bind(R.id.compass_distance)
    TextView distance;

    private int currentAzimuth = 0;

    @State
    Location target;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        // Inject Butterknife bindings
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void rotateCompass(final Float degrees) {
        // Rotate by negative degrees because Android rotates counter-clockwise
        arrow.animate()
                .rotation(-degrees)
                .setDuration(100)
                .setInterpolator(new LinearInterpolator())
                .start();

    }

    public void updateDistance(double distance) {
        this.distance.setText(String.valueOf(distance));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
