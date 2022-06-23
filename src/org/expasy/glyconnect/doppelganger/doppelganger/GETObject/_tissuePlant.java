package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class _tissuePlant {
    private final JsonObject tissuePlantJson;
    private String id;
    private String name;
    private String brendaId;

    /**
     * Main constructor
     *
     * @param tissuePlantJson Plant tissue information in json format.
     */
    public _tissuePlant(JsonObject tissuePlantJson) {
        this.tissuePlantJson = tissuePlantJson;

        if ( this.tissuePlantJson.get("id") != null )
            this.setId(this.tissuePlantJson.get("id").getAsString());

        if ( this.tissuePlantJson.get("name") != null )
            this.setName(this.tissuePlantJson.get("name").getAsString());

        if ( this.tissuePlantJson.get("brenda_id") != null )
            this.setBrendaId(this.tissuePlantJson.get("brenda_id").getAsString());
    }

    public JsonObject getTissuePlantJson() {
        return tissuePlantJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrendaId() {
        return brendaId;
    }

    public void setBrendaId(String brendaId) {
        this.brendaId = brendaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof _tissuePlant)) return false;
        _tissuePlant that = (_tissuePlant) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(brendaId, that.brendaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brendaId);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
