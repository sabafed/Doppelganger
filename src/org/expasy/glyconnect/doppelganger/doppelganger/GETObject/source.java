package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class source {
    private final JsonObject sourceJson;
    private _tissue tissue;
    private _cellLine cellLine;
    private _cellType cellType;
    private _cellComponent cellComponent;
    private _tissuePlant tissuePlant;

    /**
     * Main constructor
     *
     * @param sourceJson Source information in json format.
     */
    public source(JsonObject sourceJson) {
        this.sourceJson = sourceJson;

        if ( sourceJson.get("tissue") != null )         this.setTissue();
        if ( sourceJson.get("cell_line") != null )      this.setCellLine();
        if ( sourceJson.get("cell_type") != null )      this.setCellType();
        if ( sourceJson.get("cell_component") != null ) this.setCellComponent();
        if ( sourceJson.get("tissue_plant") != null )   this.setTissuePlant();
    }

    public JsonObject getSourceJson() {
        return sourceJson;
    }

    public _tissue getTissue() {
        return tissue;
    }

    public void setTissue() {
        this.tissue = new _tissue(this.sourceJson);
    }

    public _cellLine getCellLine() {
        return cellLine;
    }

    public void setCellLine() {
        this.cellLine = new _cellLine(this.sourceJson);
    }

    public _cellType getCellType() {
        return cellType;
    }

    public void setCellType() {
        this.cellType = new _cellType(this.sourceJson);
    }

    public _cellComponent getCellComponent() {
        return cellComponent;
    }

    public void setCellComponent() {
        this.cellComponent = new _cellComponent(this.sourceJson);
    }

    public _tissuePlant getTissuePlant() {
        return tissuePlant;
    }

    public void setTissuePlant() {
        this.tissuePlant = new _tissuePlant(this.sourceJson);
    }

    @Override
    public String toString() {
        return this.sourceJson.toString();
    }
}
