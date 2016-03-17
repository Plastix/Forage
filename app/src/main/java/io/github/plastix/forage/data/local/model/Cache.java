package io.github.plastix.forage.data.local.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import io.github.plastix.forage.data.api.gson.HtmlAdapter;
import io.github.plastix.forage.data.api.gson.RealmLocationAdapter;
import io.github.plastix.forage.data.api.gson.StringCapitalizer;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm model of a Geocache object.
 */
public class Cache extends RealmObject {

    @PrimaryKey
    @SerializedName("code")
    public String cacheCode; // Opencaching ID

    public String name; //Name of the Cache

    @JsonAdapter(RealmLocationAdapter.class)
    public RealmLocation location;

    @JsonAdapter(StringCapitalizer.class)
    public String type; // Cache type such as "Traditional, Multi, Quiz, Virtual"

    @JsonAdapter(StringCapitalizer.class)
    public String status; //Cache Status "Available, etc"

    public float terrain; //Terrain rating of cache
    public float difficulty; //Difficulty rating of cache

    @SerializedName("size2")
    public String size; // String size of container "none, nano"

    @JsonAdapter(HtmlAdapter.class)
    public String description; //HTML Description of the cache


}
