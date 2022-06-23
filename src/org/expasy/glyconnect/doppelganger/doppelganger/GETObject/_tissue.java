package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class _tissue {
    private final JsonObject tissueJson;
    private String id;
    private String name;
    private String uberonId;

    /**
     * Main constructor
     *
     * @param tissueJson Tissue information in json format.
     */
    public _tissue(JsonObject tissueJson) {
        this.tissueJson = tissueJson;

        if ( this.tissueJson.get("id") != null )
            this.setId(this.tissueJson.get("id").getAsString());

        if ( this.tissueJson.get("name") != null )
            this.setName(this.tissueJson.get("name").getAsString());

        if ( this.tissueJson.get("uberon_id") != null )
            this.setUberonId(this.tissueJson.get("uberon_id").getAsString());
    }

    public JsonObject getTissuePlantJson() {
        return tissueJson;
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

    public String getUberonId() {
        return uberonId;
    }

    public void setUberonId(String uberonId) {
        this.uberonId = uberonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof _tissue)) return false;
        _tissue tissue = (_tissue) o;
        return Objects.equals(id, tissue.id) &&
                Objects.equals(name, tissue.name) &&
                Objects.equals(uberonId, tissue.uberonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uberonId);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
