package org.expasy.glyconnect.doppelganger.doppelganger.POSTObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class POSTObject {
    private List<node> nodes;
    private List<link> links;

    /**
     * Main constructor
     * @param POSTSection The POST response containing nodes and links in json format.
     */
    public POSTObject(JsonObject POSTSection) {
        //System.out.println(POSTSection);
        JsonArray nodeArray = POSTSection.get("nodes").getAsJsonArray();

        for (JsonElement na : nodeArray) {
            //System.out.println(na.getAsJsonObject().get("averageMass"));
        }
    }
}
