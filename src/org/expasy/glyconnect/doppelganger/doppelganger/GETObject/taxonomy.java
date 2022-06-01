package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class taxonomy {
    private final JsonObject taxonomyJson;
    private String commonName;
    private String id;
    private String taxonomyId;
    private String species;

    /**
     * Main constructor
     *
     * @param taxonomyJson Taxonomy information in json format.
     */
    public taxonomy(JsonObject taxonomyJson) {
        this.taxonomyJson = taxonomyJson;
        if ( this.taxonomyJson.get("common_name") != null )
            this.setCommonName(this.taxonomyJson.get("common_name").getAsString());
        this.setId(this.taxonomyJson.get("id").getAsString());
        this.setTaxonomyId(this.taxonomyJson.get("taxonomy_id").getAsString());
        this.setSpecies(this.taxonomyJson.get("species").getAsString());
    }
    public JsonObject getTaxonomyJson() {
        return taxonomyJson;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return this.taxonomyId+";"+this.species;
    }
}
