package io.github.plastix.forage.util;

import android.annotation.SuppressLint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static com.google.common.truth.Truth.assertThat;

@RunWith(Parameterized.class)
public class UtilConstructorTests {

    private Class clazz;

    public UtilConstructorTests(Class clazz) {
        this.clazz = clazz;
    }

    @Parameterized.Parameters
    public static Object[] objects() {
        return new Object[]{
                ActivityUtils.class,
                AngleUtils.class,
                LocationUtils.class,
                MenuUtils.class,
                PermissionUtils.class,
                RxUtils.class,
                StringUtils.class,
                UnitUtils.class
        };
    }

    @SuppressLint("NewApi")
    @Test
    public void constructorShouldBePrivate() {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            assertThat(Modifier.isPrivate(constructor.getModifiers()));
        }

        constructors[0].setAccessible(true);
        try {
            constructors[0].newInstance();
        } catch (UnsupportedOperationException e) {
            // Pass
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.fillInStackTrace();
        }
    }
}
