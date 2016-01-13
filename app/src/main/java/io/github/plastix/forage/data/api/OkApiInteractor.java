package io.github.plastix.forage.data.api;

import android.location.Location;

import com.google.gson.JsonArray;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.plastix.forage.data.UseCase;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class OkApiInteractor extends UseCase {

    private OkApi api;

    @Inject
    public OkApiInteractor(OkApi api) {
        this.api = api;
    }

    public void getNearbyCaches(Location location, SingleSubscriber<JsonArray> subscriber) {
        this.subscription = api.getNearbyCaches(location)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
