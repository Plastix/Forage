package io.github.plastix.forage.data.api.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Factory for {@link ListTypeAdapter}. Uses some hacky Gson internal APis for figuring out the
 * correct type needed for the list.
 */
public class ListTypeAdapterFactory implements TypeAdapterFactory {

    /**
     * No argument @Inject constructor so Dagger can instantiate this for us.
     */
    @Inject
    public ListTypeAdapterFactory() {
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (!List.class.equals(type.getRawType())) {
            return null;
        }

        // Extract the collection type. WARNING: Uses Gson's internal APIs.
        Type collectionType =
                $Gson$Types.getCollectionElementType(type.getType(), type.getRawType());
        // Create a TypeAdapter for the collection type.
        TypeAdapter<?> delegateAdapter = gson.getAdapter(TypeToken.get(collectionType));

        //noinspection unchecked
        return (TypeAdapter<T>) new ListTypeAdapter<>(delegateAdapter).nullSafe();
    }
}

