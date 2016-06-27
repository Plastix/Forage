package io.github.plastix.forage;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.annotation.NonNull;

// Custom runner allows us set config in one place instead of setting it in each test class.
public class ForageFunctionalTestRunner extends AndroidJUnitRunner {

    @Override
    @NonNull
    public Application newApplication(@NonNull ClassLoader cl, @NonNull String className, @NonNull Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return Instrumentation.newApplication(ForageFunctionalTestApplication.class, context);
    }
}
