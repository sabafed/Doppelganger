package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class _cellType {
    private JsonObject cellTypeJson;
    private String id;
    private String name;
    private String cellOntologyId;

    /**
     * Main constructor
     *
     * @param cellTypeJson Cell type information in json format.
     */
    public _cellType(JsonObject cellTypeJson) {
        this.cellTypeJson = cellTypeJson;
        if ( this.cellTypeJson.get("id") != null )
            this.setId(this.cellTypeJson.get("id").getAsString());

        if ( this.cellTypeJson.get("name") != null )
            this.setName(this.cellTypeJson.get("name").getAsString());

        if ( this.cellTypeJson.get("cell_ontology_id") != null )
            this.setCellOntologyId(this.cellTypeJson.get("cell_ontology_id").getAsString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellOntologyId() {
        return cellOntologyId;
    }

    public void setCellOntologyId(String cellOntologyId) {
        this.cellOntologyId = cellOntologyId;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
