package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonObject;

public class structure {
    private final JsonObject structureJson;
    private String id;
    private String glytoucanId;
    private String glycanCore;
    private String glycanType;
    private boolean hasImage;
    private boolean isUndefined;
    private boolean reviewed;

    /**
     * Main constructor
     *
     * @param structureJson Structore information in json format.
     */
    public structure(JsonObject structureJson) {
        this.structureJson = structureJson;
        this.setId(this.structureJson.get("id").getAsString());
        if ( this.structureJson.get("glytoucan_id") != null )
            this.setGlytoucanId(this.structureJson.get("glytoucan_id").getAsString());
        this.setGlycanCore(this.structureJson.get("glycan_core").getAsString());
        this.setGlycanType(this.structureJson.get("glycan_type").getAsString());
        this.setHasImage(this.structureJson.get("has_image").getAsBoolean());
        this.setUndefined(this.structureJson.get("is_undefined").getAsBoolean());
        this.setReviewed(this.structureJson.get("reviewed").getAsBoolean());
    }
    public JsonObject getStructureJson() {
        return structureJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlytoucanId() {
        return glytoucanId;
    }

    public void setGlytoucanId(String glytoucanId) {
        this.glytoucanId = glytoucanId;
    }

    public String getGlycanCore() {
        return glycanCore;
    }

    public void setGlycanCore(String glycanCore) {
        this.glycanCore = glycanCore;
    }

    public String getGlycanType() {
        return glycanType;
    }

    public void setGlycanType(String glycanType) {
        this.glycanType = glycanType;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isUndefined() {
        return isUndefined;
    }

    public void setUndefined(boolean undefined) {
        isUndefined = undefined;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
