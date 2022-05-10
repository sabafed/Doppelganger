package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class disease {
    private final JsonObject diseaseJson;
    private String id;
    private String doId;
    private String name;

    /**
     * Main constructor
     *
     * @param diseaseJson Disease information in json format.
     */
    public disease(JsonObject diseaseJson) {
        this.diseaseJson = diseaseJson;

        //System.out.println(diseaseJson.size());

        this.setId(this.diseaseJson.get("id").getAsString());

        if ( this.diseaseJson.get("do_id") != null)
            this.setDoId(this.diseaseJson.get("do_id").getAsString());
        this.setName(this.diseaseJson.get("name").getAsString());
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

    public JsonObject getDiseaseJson() {
        return diseaseJson;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
