package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class reference {
    private final JsonArray referenceJson;
    private String doi;
    private String id;
    private String pmid;
    private String title;
    private String authors;
    private String year;

    public int doiless;

    public reference(JsonArray referenceJson) {
        this.referenceJson = referenceJson;

        for (JsonElement je : referenceJson) {
            if ( je.getAsJsonObject().get("doi") != null ) {
                this.setDoi(je.getAsJsonObject().get("doi").getAsString());
            } else {
                doiless++;
            }
            this.setId(je.getAsJsonObject().get("id").getAsString());

            if ( je.getAsJsonObject().get("pmid") != null )
                this.setPmid(je.getAsJsonObject().get("pmid").getAsString());

            this.setTitle(je.getAsJsonObject().get("title").getAsString());
            this.setAuthors(je.getAsJsonObject().get("authors").getAsString());
            this.setYear(je.getAsJsonObject().get("year").getAsString());
        }
    }
    public JsonArray getReferenceJson() {
        return referenceJson;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
