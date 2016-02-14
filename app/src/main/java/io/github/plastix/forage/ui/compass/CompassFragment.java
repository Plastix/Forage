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
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;

/**
 * Fragment that is responsible for the geocache compass.
 */
public class CompassFragment extends Fragment implements CompassView {

    private static final String EXTRA_CACHE_LOCATION = "CACHE_LOCATION";
    private static float CENTER = 0.5f;

    @Bind(R.id.compass_arrow)
    IconicsImageView arrow;

    @Bind(R.id.compass_distance)
    TextView distance;

    @Inject
    CompassPresenter compassPresenter;

    private float currentAzimuth = 0;
    private Location target;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

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
        compassPresenter.setTargetLocation(target);
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
    public void onResume() {
        super.onResume();
        compassPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        compassPresenter.onStop();
    }
}
