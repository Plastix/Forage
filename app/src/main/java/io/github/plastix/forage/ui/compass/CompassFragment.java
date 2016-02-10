package io.github.plastix.forage.ui.compass;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.mikepenz.iconics.view.IconicsImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;

/**
 * Fragment that is responsible for the geocache compass.
 */
public class CompassFragment extends Fragment implements CompassView {

    @Bind(R.id.compass_arrow)
    IconicsImageView arrow;

    @Inject
    CompassPresenter compassPresenter;

    private float currentAzimuth = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        injectDependencies();
    }

    private void injectDependencies() {
        ForageApplication.getComponent(getContext())
                .plus(new CompassModule(this)).injectTo(this);
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
    public void rotateCompass(Float degrees) {
        Animation an = new RotateAnimation(-currentAzimuth, -degrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        an.setDuration(200);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        currentAzimuth = degrees;
        arrow.startAnimation(an);
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
