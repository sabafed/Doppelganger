package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class peptide {
    private final JsonObject peptideJson;
    private String id;
    private String length;
    private String sequence;

    /**
     * Main constructor
     *
     * @param peptideJson Peptide information in json format.
     */
    public peptide(JsonObject peptideJson) {
        this.peptideJson = peptideJson;

        this.setId(this.peptideJson.get("id").getAsString());
        this.setLength(this.peptideJson.get("length").getAsString());
        this.setSequence(this.peptideJson.get("sequence").getAsString());
    }

    public JsonObject getPeptideJson() {
        return peptideJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof peptide)) return false;
        peptide peptide = (peptide) o;
        return Objects.equals(id, peptide.id) &&
                Objects.equals(length, peptide.length) &&
                Objects.equals(sequence, peptide.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, length, sequence);
    }

    @Override
    public String toString() {
        return sequence;
    }
}
