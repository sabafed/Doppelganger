package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class POSTObject {
    private final JsonObject POSTSection;
    private final List<node> nodes = new ArrayList<>();
    private final List<link> links = new ArrayList<>();

    /**
     * Main constructor
     *
     * @param POSTSection The POST response containing nodes and links in json format.
     */
    public POSTObject(JsonObject POSTSection) {
        this.POSTSection = POSTSection;

        JsonArray nodeArray = this.POSTSection.get("nodes").getAsJsonArray();
        JsonArray linksArray = this.POSTSection.get("links").getAsJsonArray();

        for (JsonElement na : nodeArray) {
            node node = new node(na.getAsJsonObject());
            this.nodes.add(node);
            //System.out.println(node);
        }

        for (JsonElement la : linksArray) {
            link link = new link(la.getAsJsonObject());
            this.links.add(link);
            //System.out.println(link);
        }
    }

    public JsonObject getPOSTSection() {
        return POSTSection;
    }

    public List<node> getNodes() {
        return nodes;
    }

    public List<link> getLinks() {
        return links;
    }

    public boolean equals(POSTObject other) {
        return this.POSTSection.equals(other.getPOSTSection());
    }

    @Override
    public String toString() {
        return String.valueOf(this.POSTSection);
    }
}
