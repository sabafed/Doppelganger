package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class disease {
    private final JsonArray diseaseJson;
    private String id;
    private String doId;
    private String name;

    /**
     * Main constructor
     *
     * @param diseaseJson Disease information in json format.
     */
    public disease(JsonArray diseaseJson) {
        this.diseaseJson = diseaseJson;
        for (JsonElement je : diseaseJson) {
            this.setId(je.getAsJsonObject().get("id").getAsString());
            if ( je.getAsJsonObject().get("do_id") != null)
                this.setDoId(je.getAsJsonObject().get("do_id").getAsString());
            this.setName(je.getAsJsonObject().get("name").getAsString());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoId() {
        return doId;
    }

    public void setDoId(String doId) {
        this.doId = doId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonArray getDiseaseJson() {
        return diseaseJson;
    }
}
