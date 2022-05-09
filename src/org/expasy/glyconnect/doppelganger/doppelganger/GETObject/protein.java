package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class protein {
    private JsonObject proteinJson;
    private String id;
    private String name;
    private boolean reviewed;
    private List<uniprot> uniprots;

    public protein(JsonObject proteinJson) {
        this.proteinJson = proteinJson;
        if ( this.proteinJson.get("id") != null )
            this.setId(this.proteinJson.get("id").getAsString());
    }
    public JsonElement getProteinJson() {
        return proteinJson;
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

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public List<uniprot> getUniprots() {
        return uniprots;
    }

    public void setUniprots(List<uniprot> uniprots) {
        this.uniprots = uniprots;
    }
}
