package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class site {
    private JsonObject siteJson;
    private String glycoSite;
    private String location;

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
}
