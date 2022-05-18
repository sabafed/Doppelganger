package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonObject;

public class link {
    private final JsonObject linkJson;
    private String abbreviation;
    private String condensedFormat;
    private String source;
    private String target;

    /**
     * Main constructor
     *
     * @param linkJson Link information in json format.
     */
    public link(JsonObject linkJson) {
        this.linkJson = linkJson;
        this.setAbbreviation();
        this.setCondensedFormat();
        this.setSource();
        this.setTarget();
    }

    public JsonObject getLinkJson() {
        return linkJson;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation() {
        this.abbreviation = this.linkJson.get("abbreviation").getAsString();
    }

    public String getCondensedFormat() {
        return condensedFormat;
    }

    public void setCondensedFormat() {
        this.condensedFormat = this.linkJson.get("condensedFormat").getAsString();
    }

    public String getSource() {
        return source;
    }

    public void setSource() {
        this.source = this.linkJson.get("source").getAsString();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget() {
        this.target = this.linkJson.get("target").getAsString();
    }

    @Override
    public String toString() {
        return this.source+"~"+this.target;
    }
}
