package io.github.plastix.forage;

import android.support.annotation.NonNull;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

// Custom runner allows us set config in one place instead of setting it in each test class.
public class ForageRoboelectricUnitTestRunner extends RobolectricTestRunner {

    // This value should be changed as soon as Robolectric will support newer api.
    private static final int SDK_EMULATE_LEVEL = 23;

    public ForageRoboelectricUnitTestRunner(@NonNull Class<?> klass) throws Exception {
        super(klass);
    }

    @Override
    public Config getConfig(@NonNull Method method) {
        final Config defaultConfig = super.getConfig(method);
        return new Config.Implementation(
                new int[]{SDK_EMULATE_LEVEL},
                defaultConfig.manifest(),
                defaultConfig.qualifiers(),
                defaultConfig.packageName(),
                defaultConfig.abiSplit(),
                defaultConfig.resourceDir(),
                defaultConfig.assetDir(),
                defaultConfig.buildDir(),
                defaultConfig.shadows(),
                defaultConfig.instrumentedPackages(),
                ForageUnitTestApplication.class,
                defaultConfig.libraries(),
                defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
        );
    }
}
