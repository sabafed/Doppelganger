package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class POSTObject {
    private final JsonObject POSTSection;
    private final HashMap<Integer, node> nodes = new HashMap<Integer, node>();
    private final HashMap<Integer, link> links = new HashMap<Integer, link>();

    private HashMap<String, Integer> propertiesCount = new HashMap<>();

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

        this.setPropertiesCount();
    }

    public HashMap<String, Integer> getPropertiesCount() {
        return propertiesCount;
    }

    public void setPropertiesCount() {
        HashMap<String, Integer> propertiesCount = new HashMap<>();

        propertiesCount.putIfAbsent("Neutral", 0);
        propertiesCount.putIfAbsent("Fucosylated", 0);
        propertiesCount.putIfAbsent("Sialylated", 0);
        propertiesCount.putIfAbsent("Fuco-sialylated", 0);
        propertiesCount.putIfAbsent("Oligomannose", 0);
        propertiesCount.putIfAbsent("Sulfated", 0);

        for (Integer i : this.nodes.keySet()){
            for (String prop : nodes.get(i).getProperties()){
                propertiesCount.replace(prop, propertiesCount.get(prop)+1);
                /*
                Each time a property is present in a node,
                said property is get from propertiesCount
                and its value is increased by 1
                */
            }
        }
        this.propertiesCount = propertiesCount;
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

    public boolean equals(POSTObject other) {
        return this.POSTSection.equals(other.getPOSTSection());
    }

    public void attributesChecker() {
        System.out.println(
                "Nodes: "+this.getNodes()+"\n"+
                "Links: "+this.getLinks()+"\n"+
                "Glycan properties:\n"+this.getPropertiesCount()+"\n");
    }

    @Override
    public String toString() {
        return String.valueOf(this.POSTSection);
    }


}
