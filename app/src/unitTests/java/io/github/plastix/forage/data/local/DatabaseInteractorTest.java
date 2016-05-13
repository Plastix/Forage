package io.github.plastix.forage.data.local;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dagger.Lazy;
import io.realm.Realm;

import static org.mockito.Mockito.verify;

/**
 * Todo instrumentation tests for Realm
 * Realm relies heavily on native code on Android. Since we can't actually run a realm during the
 * unit tests we can only verify a few interactions with the mock. Testing the functionality of other
 * DatabaseInteractor methods must be done with instrumentation tests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Realm.class)
public class DatabaseInteractorTest {

    private DatabaseInteractor databaseInteractor;
    private Realm realm;
    private Lazy<Realm> realmLazy;

    @Before
    public void beforeEachTest() {
        realm = PowerMockito.mock(Realm.class);
        realmLazy = new Lazy<Realm>() {
            @Override
            public Realm get() {
                return realm;
            }
        };
        databaseInteractor = new DatabaseInteractor(realmLazy);
    }

    @Test
    public void onDestroy_shouldCallRealmClose() {
        databaseInteractor.onDestroy();
        verify(realm).close();
    }
}
