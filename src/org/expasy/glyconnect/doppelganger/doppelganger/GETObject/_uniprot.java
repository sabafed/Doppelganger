package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class _uniprot {
    private JsonObject uniprotJson;
    private String uniprotAcc;
    private String uniprotId;
    private String nextprot;
    private String genecards;
    private String glygen;

    /**
     * Main constructor
     *
     * @param uniprotJson Uniprot identifiers in json format.
     */
    public _uniprot(JsonObject uniprotJson) {
        this.uniprotJson = uniprotJson;

        this.setUniprotAcc(this.uniprotJson.get("uniprot_acc").getAsString());

        if ( this.uniprotJson.get("uniprot_id") != null )
            this.setUniprotId(this.uniprotJson.get("uniprot_id").getAsString());

        if ( this.uniprotJson.get("nextprot") != null )
            this.setNextprot(this.uniprotJson.get("nextprot").getAsString());

        if ( this.uniprotJson.get("genecards") != null )
            this.setGenecards(this.uniprotJson.get("genecards").getAsString());

        if ( this.uniprotJson.get("glygen") != null )
            this.setGlygen(this.uniprotJson.get("glygen").getAsString());
    }

    public JsonObject getUniprotJson() {
        return uniprotJson;
    }

    public String getUniprotAcc() {
        return uniprotAcc;
    }

    private void setUniprotAcc(String uniprotAcc) {
        this.uniprotAcc = uniprotAcc;
    }

    public String getUniprotId() {
        return uniprotId;
    }

    private void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    public String getNextprot() {
        return nextprot;
    }

    private void setNextprot(String nextprot) {
        this.nextprot = nextprot;
    }

    public String getGenecards() {
        return genecards;
    }

    private void setGenecards(String genecards) {
        this.genecards = genecards;
    }

    public String getGlygen() {
        return glygen;
    }

    private void setGlygen(String glygen) {
        this.glygen = glygen;
    }

    @Override
    public String toString(){
        return this.uniprotAcc;
    }
}
