package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonObject;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof link)) return false;
        link link = (link) o;
        return Objects.equals(abbreviation, link.abbreviation) &&
                Objects.equals(condensedFormat, link.condensedFormat) &&
                Objects.equals(source, link.source) &&
                Objects.equals(target, link.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abbreviation, condensedFormat, source, target);
    }

    @Override
    public String toString() {
        return this.source+"~"+this.target;
    }
}
