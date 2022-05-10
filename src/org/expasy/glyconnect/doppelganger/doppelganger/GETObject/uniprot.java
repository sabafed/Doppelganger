package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class uniprot {
    private JsonArray uniprotJson;
    private String uniprotAcc;
    private String uniprotId;
    private String nextprot;
    private String genecards;
    private String glygen;

    public uniprot(JsonArray uniprotJson) {
        this.uniprotJson = uniprotJson;
        for (JsonElement je : uniprotJson) {
            this.setUniprotAcc(je.getAsJsonObject().get("uniprot_acc").getAsString());
            this.setUniprotId(je.getAsJsonObject().get("uniprot_id").getAsString());

            if ( je.getAsJsonObject().get("nextprot") != null )
                this.setNextprot(je.getAsJsonObject().get("nextprot").getAsString());

            if ( je.getAsJsonObject().get("genecards") != null )
                this.setGenecards(je.getAsJsonObject().get("genecards").getAsString());

            if ( je.getAsJsonObject().get("glygen") != null )
                this.setGlygen(je.getAsJsonObject().get("glygen").getAsString());
        }
    }

    public JsonArray getUniprotJson() {
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
