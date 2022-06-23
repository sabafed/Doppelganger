package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class protein {
    private final JsonObject proteinJson;
    private String id;
    private String name;
    private boolean reviewed;
    private final List<_uniprot> uniprots = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param proteinJson Proteins information in json format.
     */
    public protein(JsonObject proteinJson) {
        this.proteinJson = proteinJson;
        this.setId();
        this.setName();
        this.setReviewed();
        this.setUniprots();
    }
    public JsonElement getProteinJson() {
        return proteinJson;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        if ( !this.proteinJson.get("id").isJsonNull() )
            this.id = this.proteinJson.get("id").getAsString();
    }

    public String getName() {
        return name;
    }

    public void setName() {
        if ( !this.proteinJson.get("name").isJsonNull() )
            this.name = this.proteinJson.get("name").getAsString();
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed() {
        if ( !this.proteinJson.get("reviewed").isJsonNull() )
            this.reviewed = this.proteinJson.get("reviewed").getAsBoolean();
    }

    public List<_uniprot> getUniprots() {
        return uniprots;
    }

    public void setUniprots() {
        if ( this.proteinJson.get("uniprots") != null ) {
            JsonArray uniprotArray = this.proteinJson.get("uniprots").getAsJsonArray();
            for (JsonElement je : uniprotArray) {
                _uniprot uniprot = new _uniprot(je.getAsJsonObject());
                this.uniprots.add(uniprot);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof protein)) return false;
        protein protein = (protein) o;
        return reviewed == protein.reviewed &&
                Objects.equals(id, protein.id) &&
                Objects.equals(name, protein.name) &&
                Objects.equals(uniprots, protein.uniprots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reviewed, uniprots);
    }

    @Override
    public String toString() {
        return this.uniprots.toString();
    }
}
