package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class node {
    private final JsonObject nodesJson;
    private String id;
    private String glytoucanId;

    private String glyconnectFormat;
    private String byonicFormat;
    private String condensedFormat;

    private String averageMass;
    private String monoisotopicMass;

    private String label;
    private String glycanType;

    private boolean virtual;

    private String parentCount;
    private String childCount;

    private final ArrayList<String> parents = new ArrayList<>();
    private final ArrayList<String> children = new ArrayList<>();
    private final ArrayList<String> structures = new ArrayList<>();
    private final ArrayList<String> references = new ArrayList<>();

    private ArrayList<String> properties = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param nodesJson Nodes information in json format.
     */
    public node(JsonObject nodesJson) {
        this.nodesJson = nodesJson;
        this.setId();
        this.setGlytoucanId();

        this.setGlyconnectFormat();
        this.setByonicFormat();
        this.setCondensedFormat();

        this.setAverageMass();
        this.setMonoisotopicMass();

        this.setLabel();
        this.setGlycanType();

        this.setVirtual();

        this.setParentCount();
        this.setChildCount();

        this.setParents();
        this.setChildren();
        this.setStructures();
        this.setReferences();

        this.setProperties();
    }
    public JsonObject getNodesJson() {
        return nodesJson;
    }

    public ArrayList<String> getProperties() {
        return properties;
    }

    public void setProperties() {
        ArrayList<String> properties = new ArrayList<>();

        String[] glyconnectFormat = this.glyconnectFormat.split(" ");

        boolean hex = false;   // for oligomannose: node has number of hex >= 5
        boolean hexNAc = true; //                   node has number of hexNac <= 2
        boolean dHex = true;   //                   node has number of dHex <= 1

        for (String residue : glyconnectFormat) {
            if ( residue.contains("Su") && !properties.contains("Sulfated") )
                properties.add("Sulfated");

            if ( residue.contains("dHex") && !properties.contains("Fucosylated") ) {
                properties.add("Fucosylated");
                String[] fucose = residue.split(":");
                if ( Integer.parseInt(fucose[1]) > 1 ) dHex = false;
            }

            if ( residue.contains("Hex") ) {
                String[] pentose = residue.split(":");
                if ( Integer.parseInt(pentose[1]) > 4 ) hex = true;
            }

            if ( residue.contains("HexNAc") ) {
                String[] hexosamine = residue.split(":");
                if ( Integer.parseInt(hexosamine[1]) > 2 ) hexNAc = false;
            }

            if ( (residue.contains("NeuAc")
                    || residue.contains("NeuGc"))
                    && !properties.contains("Sialylated") )
                properties.add("Sialylated");
        }

        if ( hex && !properties.contains("Oligomannose") ) {
            if ( hexNAc && dHex ) properties.add("Oligomannose");
        }

        if ( properties.contains("Fucosylated")
                && properties.contains("Sialylated")
                && !properties.contains("Fuco-sialylated") )
            properties.add("Fuco-sialylated");

        if ( !properties.contains("Sialylated")
                && !properties.contains("Sulfated")
                && !properties.contains("Neutral") )
            properties.add("Neutral");

        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = this.nodesJson.get("id").getAsString();
    }

    public String getGlytoucanId() {
        return glytoucanId;
    }

    public void setGlytoucanId() {
        this.glytoucanId = this.nodesJson.get("glytoucanId").getAsString();
    }

    public String getGlyconnectFormat() {
        return glyconnectFormat;
    }

    public void setGlyconnectFormat() {
        this.glyconnectFormat = this.nodesJson.get("glyconnectFormat").getAsString();
    }

    public String getByonicFormat() {
        return byonicFormat;
    }

    public void setByonicFormat() {
        if ( this.nodesJson.get("byonicFormat") != null )
            this.byonicFormat = this.nodesJson.get("byonicFormat").getAsString();
    }

    public String getCondensedFormat() {
        return condensedFormat;
    }

    public void setCondensedFormat() {
        this.condensedFormat = this.nodesJson.get("condensedFormat").getAsString();
    }

    public String getAverageMass() {
        return averageMass;
    }

    public void setAverageMass() {
        this.averageMass = this.nodesJson.get("averageMass").getAsString();
    }

    public String getMonoisotopicMass() {
        return monoisotopicMass;
    }

    public void setMonoisotopicMass() {
        this.monoisotopicMass = this.nodesJson.get("monoisotopicMass").getAsString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel() {
        this.label = this.nodesJson.get("label").getAsString();
    }

    public String getGlycanType() {
        return glycanType;
    }

    public void setGlycanType() {
        this.glycanType = this.nodesJson.get("glycanType").getAsString();
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual() {
        this.virtual = this.nodesJson.get("virtual").getAsBoolean();
    }

    public String getParentCount() {
        return parentCount;
    }

    public void setParentCount() {
        this.parentCount = this.nodesJson.get("parentCount").getAsString();
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount() {
        this.childCount = this.nodesJson.get("childCount").getAsString();
    }

    public ArrayList<String> getParents() {
        return parents;
    }

    public void setParents() {
        JsonArray parentsArray = this.nodesJson.get("parents").getAsJsonArray();
        for (JsonElement pa : parentsArray) parents.add(pa.getAsString());
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren() {
        JsonArray childrenArray = this.nodesJson.get("children").getAsJsonArray();
        for (JsonElement ca : childrenArray) children.add(ca.getAsString());
    }

    public ArrayList<String> getStructures() {
        return structures;
    }

    public void setStructures() {
        JsonArray structuresArray = this.nodesJson.get("structures").getAsJsonArray();
        for (JsonElement sa : structuresArray) structures.add(sa.getAsString());
    }

    public ArrayList<String> getReferences() {
        return references;
    }

    public void setReferences() {
        JsonArray referencesArray = this.nodesJson.get("references").getAsJsonArray();
        for (JsonElement ra : referencesArray) references.add(ra.getAsString());
    }

    public boolean equals(node other) {
        if ( this.id != null && other.id != null && this.id.equals(other.id)){
            if (this.condensedFormat != null && other.condensedFormat != null &&
            this.condensedFormat.equals(other.condensedFormat)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.condensedFormat;
    }
}
