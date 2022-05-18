package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class POSTObject {
    private final JsonObject POSTSection;
    private final Map<Integer, node> nodes = new HashMap<Integer, node>();
    private final Map<Integer, link> links = new HashMap<Integer, link>();

    /**
     * Main constructor
     *
     * @param POSTSection The POST response containing nodes and links in json format.
     */
    public POSTObject(JsonObject POSTSection) {
        this.POSTSection = POSTSection;

        JsonArray nodeArray = this.POSTSection.get("nodes").getAsJsonArray();
        int nodeIndex = 0;

        JsonArray linksArray = this.POSTSection.get("links").getAsJsonArray();
        int linkIndex = 0;

        for (JsonElement na : nodeArray) {
            node node = new node(na.getAsJsonObject());
            this.nodes.put(nodeIndex, node);
            nodeIndex++;
            //System.out.println(node);
        }

        for (JsonElement la : linksArray) {
            link link = new link(la.getAsJsonObject());
            this.links.put(linkIndex, link);
            linkIndex++;
            //System.out.println(link);
        }
    }

    public JsonObject getPOSTSection() {
        return POSTSection;
    }

    public Map<Integer, node> getNodes() {
        return nodes;
    }

    public Map<Integer, link> getLinks() {
        return links;
    }

    public boolean equals(POSTObject other) {
        return this.POSTSection.equals(other.getPOSTSection());
    }

    public void attributesChecker() {
        System.out.println(
                "Nodes: "+this.getNodes()+"\n"+
                "Links: "+this.getLinks()+"\n" );
    }

    @Override
    public String toString() {
        return String.valueOf(this.POSTSection);
    }
}
