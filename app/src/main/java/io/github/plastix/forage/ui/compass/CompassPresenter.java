package io.github.plastix.forage.ui.compass;

import javax.inject.Inject;

import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.LifecycleCallbacks;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class CompassPresenter implements LifecycleCallbacks {

    private CompassView view;
    private AzimuthInteractor asimuthInteractor;
    private Subscription subscription;

    @Inject
    public CompassPresenter(CompassView view, AzimuthInteractor azimuthInteractor) {
        this.view = view;
        this.asimuthInteractor = azimuthInteractor;
        this.subscription = Subscriptions.empty();
    }

    public void updateCompass() {
        unSubscribe();
        this.subscription = asimuthInteractor.getAzimuthObservable().subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                view.rotateCompass(aFloat);
            }
        });
    }

    private void unSubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        unSubscribe();
    }

    @Override
    public void onResume() {
        updateCompass();
    }
}
