package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;

public class uniprot {
    private JsonArray uniprotJson;
    private String uniprotAcc;
    private String uniprotId;
    private String nextprot;
    private String genecards;
    private String glygen;

    public uniprot(JsonArray uniprotJson) {
        this.uniprotJson = uniprotJson;
    }

    public JsonArray getUniprotJson() {
        return uniprotJson;
    }

    public String getUniprotAcc() {
        return uniprotAcc;
    }

    public void setUniprotAcc(String uniprotAcc) {
        this.uniprotAcc = uniprotAcc;
    }

    public String getUniprotId() {
        return uniprotId;
    }

    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    public String getNextprot() {
        return nextprot;
    }

    public void setNextprot(String nextprot) {
        this.nextprot = nextprot;
    }

    public String getGenecards() {
        return genecards;
    }

    public void setGenecards(String genecards) {
        this.genecards = genecards;
    }

    public String getGlygen() {
        return glygen;
    }

    public void setGlygen(String glygen) {
        this.glygen = glygen;
    }
}
