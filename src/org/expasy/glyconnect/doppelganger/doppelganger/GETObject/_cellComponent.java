package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class _cellComponent {
    private final JsonObject cellComponentJson;
    private String id;
    private String name;
    private String goId;

    /**
     * Main constructor
     *
     * @param cellComponentJson Cell component information in json format.
     */
    public _cellComponent(JsonObject cellComponentJson) {
        this.cellComponentJson = cellComponentJson;

        if ( this.cellComponentJson.get("id") != null )
            this.setId(this.cellComponentJson.get("id").getAsString());

        if ( this.cellComponentJson.get("name") != null )
            this.setName(this.cellComponentJson.get("name").getAsString());

        if ( this.cellComponentJson.get("go_id") != null )
            this.setGoId(this.cellComponentJson.get("go_id").getAsString());
    }

    public JsonObject getCellComponentJson() {
        return cellComponentJson;
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

    public String getGoId() {
        return goId;
    }

    public void setGoId(String goId) {
        this.goId = goId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof _cellComponent)) return false;
        _cellComponent that = (_cellComponent) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(goId, that.goId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, goId);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
