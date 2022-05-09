package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class peptide {
    private final JsonArray peptideJson;
    private String id;
    private String length;
    private String sequence;

    public peptide(JsonArray peptideJson) {
        this.peptideJson = peptideJson;
        for (JsonElement je : peptideJson) {
            this.setId(je.getAsJsonObject().get("id").getAsString());
            this.setLength(je.getAsJsonObject().get("length").getAsString());
            this.setSequence(je.getAsJsonObject().get("sequence").getAsString());
        }
    }

    public JsonArray getPeptideJson() {
        return peptideJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
