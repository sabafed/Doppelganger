package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class reference {
    private final JsonObject referenceJson;
    private String doi;
    private String id;
    private String pmid;
    private String title;
    private String authors;
    private String year;

    public int doiless;

    public reference(JsonObject referenceJson) {
        this.referenceJson = referenceJson;

        if ( this.referenceJson.get("doi") != null ) {
            this.setDoi(this.referenceJson.get("doi").getAsString());
        } else { doiless++; }

        this.setId(this.referenceJson.get("id").getAsString());

        if ( this.referenceJson.get("pmid") != null )
            this.setPmid(this.referenceJson.get("pmid").getAsString());

        this.setTitle(this.referenceJson.get("title").getAsString());
        this.setAuthors(this.referenceJson.get("authors").getAsString());
        this.setYear(this.referenceJson.get("year").getAsString());
    }
    public JsonObject getReferenceJson() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof reference)) return false;
        reference reference = (reference) o;
        return Objects.equals(doi, reference.doi) &&
                Objects.equals(id, reference.id) &&
                Objects.equals(pmid, reference.pmid) &&
                Objects.equals(title, reference.title) &&
                Objects.equals(authors, reference.authors) &&
                Objects.equals(year, reference.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi, id, pmid, title, authors, year);
    }

    @Override
    public String toString() {
        return this.doi;
    }
}
