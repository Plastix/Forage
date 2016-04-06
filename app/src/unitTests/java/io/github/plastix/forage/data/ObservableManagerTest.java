package io.github.plastix.forage.data;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ObservableManagerTest {

    private ObservableManager observableManager;

    @Before
    public void beforeEachTest() {
        observableManager = new ObservableManager();
    }

    @Test
    public void storeObservable_callsCache() {
        String id = "id";
        Observable observable = spy(Observable.empty());

        observableManager.storeObservable(id, observable);
        verify(observable).cache();
    }


    @Test
    public void isStored_shouldReturnTrue() {
        String id = "id";
        observableManager.storeObservable(id, Observable.empty());
        assertThat(observableManager.isStored(id)).isTrue();
    }

    @Test
    public void isStored_shouldReturnFalse() {
        String id = "id";
        assertThat(observableManager.isStored(id)).isFalse();
    }

    @Test
    public void getObservable_shouldReturnStoredObservable() {
        String id = "id";
        Observable in = Observable.empty();
        observableManager.storeObservable(id, in);

        Observable observable = observableManager.getObservable(id);

        assertThat(observable).isNotNull();
    }

    @Test
    public void getObservable_shouldReturnNull() {
        String id = "id";
        Observable observable = observableManager.getObservable(id);

        assertThat(observable).isNull();
    }

    @Test
    public void remove_shouldRemoveObservable() {
        String id = "id";
        Observable in = Observable.empty();
        observableManager.storeObservable(id, in);

        assertThat(observableManager.isStored(id)).isTrue();
        observableManager.removeObservable(id);
        assertThat(observableManager.isStored(id)).isFalse();
        assertThat(observableManager.getObservable(id)).isNull();
    }

    @Test
    public void remove_removingUncachedObservableDoesNothing() {
        String id = "id";
        assertThat(observableManager.isStored(id)).isFalse();
        observableManager.removeObservable(id);
        assertThat(observableManager.isStored(id)).isFalse();
        assertThat(observableManager.getObservable(id)).isNull();
    }
}
