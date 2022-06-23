package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Objects;

public class POSTObject {
    private final JsonObject POSTSection;
    private final HashMap<Integer, node> nodes = new HashMap<>();
    private final HashMap<Integer, link> links = new HashMap<>();


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
        }

        for (JsonElement la : linksArray) {
            link link = new link(la.getAsJsonObject());
            this.links.put(linkIndex, link);
            linkIndex++;
        }
    }

    public JsonObject getPOSTSection() {
        return POSTSection;
    }

    public HashMap<Integer, node> getNodes() {
        return nodes;
    }

    public HashMap<Integer, link> getLinks() {
        return links;
    }

    public void attributesChecker() {
        System.out.println(
                "Nodes: "+this.getNodes()+"\n"+
                "Links: "+this.getLinks()+"\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POSTObject)) return false;
        POSTObject that = (POSTObject) o;
        return nodes.equals(that.nodes) && Objects.equals(links, that.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, links);
    }

    @Override
    public String toString() {
        return String.valueOf(this.POSTSection);
    }


}
