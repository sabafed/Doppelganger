package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof source)) return false;
        source source = (source) o;
        return Objects.equals(tissue, source.tissue) &&
                Objects.equals(cellLine, source.cellLine) &&
                Objects.equals(cellType, source.cellType) &&
                Objects.equals(cellComponent, source.cellComponent) &&
                Objects.equals(tissuePlant, source.tissuePlant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tissue, cellLine, cellType, cellComponent, tissuePlant);
    }

    @Override
    public String toString() {
        return this.sourceJson.toString();
    }
}
