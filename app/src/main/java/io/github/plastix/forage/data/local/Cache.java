package io.github.plastix.forage.data.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cache extends RealmObject {

    @PrimaryKey
    private String code; // Opencaching ID

    private String name; //Name of the Cache
    private String location; //String "lat|lon" of the
    private String type; // Cache type such as "Traditional, Multi, Quiz, Virtual"
    private String status; //Cache Status "Available, etc"
    private float terrain; //Terrain rating of cache
    private float difficulty; //Difficulty rating of cache
    private String size2; // String size of container "none, nano"
    private String description; //HTML Description of the cache


    // Getters and Setters used by Realm Proxy Classes

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
