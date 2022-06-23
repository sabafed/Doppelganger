package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

import java.util.Objects;

public class composition {
    private final JsonObject compositionJson;
    private String id;
    private String glytoucanId;
    private String glyconnectFormat;
    private String byonicFormat;
    private String condensedFormat;
    private double averageMass;
    private double monoisotopicMass;
    private String label = "A";
    private String glycanType = "Unknown";

    /**
     * Main constructor
     *
     * @param compositionJson Composition information in json format.
     */
    public composition(JsonObject compositionJson) {
        this.compositionJson = compositionJson;
        this.setId();
        this.setGlytoucanId();
        this.setGlyconnectFormat();
        this.setByonicFormat();
        this.setCondensedFormat();
        this.setAverageMass();
        this.setMonoisotopicMass();
        /*
        this.setLabel();
        this.seGlycanType();
         */
    }

    public String getCompositionJson(){
        return this.compositionJson.getAsString();
    }
    public void setId() {
        if ( this.compositionJson.get("id") != null )
            this.id = this.compositionJson.get("id").getAsString();
    }

    public void setGlytoucanId() {
        if ( this.compositionJson.get("glytoucan_id") != null )
            this.glytoucanId = this.compositionJson.get("glytoucan_id").getAsString();
    }

    public void setGlyconnectFormat() {
        if ( this.compositionJson.get("format_glyconnect") != null )
            this.glyconnectFormat = this.compositionJson.get("format_glyconnect").getAsString();
    }

    public void setByonicFormat() {
        if ( this.compositionJson.get("format_byonic") != null )
            this.byonicFormat = this.compositionJson.get("format_byonic").getAsString();
    }

    public void setCondensedFormat() {
        if ( this.compositionJson.get("format_condensed") != null )
            this.condensedFormat = this.compositionJson.get("format_condensed").getAsString();
    }

    public void setAverageMass() {
        if ( this.compositionJson.get("mass") != null )
            this.averageMass = this.compositionJson.get("mass").getAsDouble();
    }

    public void setMonoisotopicMass() {
        if ( this.compositionJson.get("mass_monoisotopic") != null )
            this.monoisotopicMass = this.compositionJson.get("mass_monoisotopic").getAsDouble();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setGlycanType(String glycanType) {
        //if class GETObject is N-Linked, are its compositions supposed to be N-Linked too?
        this.glycanType = glycanType;
    }

    public String getId() {
        return id;
    }

    public String getGlytoucanId() {
        return glytoucanId;
    }

    public String getGlyconnectFormat() {
        return glyconnectFormat;
    }

    public String getByonicFormat() {
        return byonicFormat;
    }

    public String getCondensedFormat() {
        return condensedFormat;
    }

    public double getAverageMass() {
        return averageMass;
    }

    public double getMonoisotopicMass() {
        return monoisotopicMass;
    }

    public String getLabel() {
        return label;
    }

    public String getGlycanType() {
        return glycanType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof composition)) return false;
        composition that = (composition) o;
        return Double.compare(that.averageMass, averageMass) == 0 &&
                Double.compare(that.monoisotopicMass, monoisotopicMass) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(glytoucanId, that.glytoucanId) &&
                Objects.equals(glyconnectFormat, that.glyconnectFormat) &&
                Objects.equals(byonicFormat, that.byonicFormat) &&
                Objects.equals(condensedFormat, that.condensedFormat) &&
                Objects.equals(label, that.label) &&
                Objects.equals(glycanType, that.glycanType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                glytoucanId,
                glyconnectFormat,
                byonicFormat,
                condensedFormat,
                averageMass,
                monoisotopicMass,
                label,
                glycanType);
    }

    @Override
    public String toString() {
        return glyconnectFormat;
    }
}
