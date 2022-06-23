package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class site {
    private JsonObject siteJson;
    private String glycoSite;
    private String location;

    /**
     * Main constructor
     *
     * @param siteJson Site information in json format.
     */
    public site(JsonObject siteJson) {
        this.siteJson = siteJson;

        if ( this.siteJson.get("glyco_site") != null )
            this.setGlycoSite(this.siteJson.get("glyco_site").getAsString());

        if ( this.siteJson.get("location") != null )
            this.setLocation(this.siteJson.get("location").getAsString());
    }
    public JsonObject getSiteJson() {
        return siteJson;
    }

    public String getGlycoSite() {
        return glycoSite;
    }

    public void setGlycoSite(String glycoSite) {
        this.glycoSite = glycoSite;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof site)) return false;
        site site = (site) o;
        return Objects.equals(glycoSite, site.glycoSite) &&
                Objects.equals(location, site.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(glycoSite, location);
    }

    @Override
    public String toString() {
        return this.glycoSite;
    }
}
