package io.github.plastix.forage.data.local;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import io.github.plastix.forage.data.api.gson.HtmlAdapter;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm model of a Geocache object.
 */
public class Cache extends RealmObject {

    @PrimaryKey
    @SerializedName("code")
    private String cacheCode; // Opencaching ID

    private String name; //Name of the Cache
    private String location;

    private String type; // Cache type such as "Traditional, Multi, Quiz, Virtual"
    private String status; //Cache Status "Available, etc"
    private float terrain; //Terrain rating of cache
    private float difficulty; //Difficulty rating of cache

    @SerializedName("size2")
    private String size; // String size of container "none, nano"

    @JsonAdapter(HtmlAdapter.class)
    private String description; //HTML Description of the cache


    // IDE generated Getters and Setters used by Realm Proxy Classes
    // These are REQUIRED!

    public String getCacheCode() {
        return cacheCode;
    }

    public void setCacheCode(String cacheCode) {
        this.cacheCode = cacheCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTerrain() {
        return terrain;
    }

    public void setTerrain(float terrain) {
        this.terrain = terrain;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
