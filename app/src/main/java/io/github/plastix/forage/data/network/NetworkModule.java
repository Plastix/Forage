package io.github.plastix.forage.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;

@Module
public class NetworkModule {

    @NonNull
    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager(@NonNull @ForApplication Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

}
