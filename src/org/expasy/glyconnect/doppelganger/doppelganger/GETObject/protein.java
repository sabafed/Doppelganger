package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class protein {
    private JsonObject proteinJson;
    private String id;
    private String name;
    private boolean reviewed;
    private List<uniprot> uniprots = new ArrayList<>();

    public protein(JsonObject proteinJson) {
        this.proteinJson = proteinJson;
        this.setId();
        this.setName();
        this.setReviewed();
        this.setUniprots();
    }
    public JsonElement getProteinJson() {
        return proteinJson;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        if ( !this.proteinJson.get("id").isJsonNull() )
            this.id = this.proteinJson.get("id").getAsString();
    }

    public String getName() {
        return name;
    }

    public void setName() {
        if ( !this.proteinJson.get("name").isJsonNull() )
            this.name = this.proteinJson.get("name").getAsString();
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed() {
        if ( !this.proteinJson.get("reviewed").isJsonNull() )
            this.reviewed = this.proteinJson.get("reviewed").getAsBoolean();
    }

    public List<uniprot> getUniprots() {
        return uniprots;
    }

    public void setUniprots() {
        uniprot uniprot =
                new uniprot(JsonParser.parseString("[]").getAsJsonArray());

        if ( this.proteinJson.get("uniprots") != null ) {
            uniprot = new uniprot(this.proteinJson.get("uniprots").getAsJsonArray());
            //this.uniprots.add(uniprot);
        }
        this.uniprots.add(uniprot);
    }

/*
    @Override
    public String toString() {
        List<String> uniprotList = new ArrayList<>();
        for (uniprot uniprot : uniprots) {
            if ( !uniprotList.contains(uniprot.getUniprotAcc()))
                uniprotList.add(uniprot.getUniprotAcc());
        }

        System.out.println(uniprotList.size());

        Gson gson = new Gson();
        JsonElement element =
                gson.toJsonTree(uniprotList,
                        new TypeToken<List<String>>(){}.getType());

        if ( element.isJsonArray() && !element.isJsonNull()) {
            JsonArray uniprotArray = uniprotArray = element.getAsJsonArray();
            if ( !uniprotArray.isJsonNull() && uniprotArray != null ) {
                //System.out.println(uniprotArray);
                return uniprotArray.getAsString();
            }
        }

        return "[]";
    }
*/
}
