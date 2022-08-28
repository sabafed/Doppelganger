package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class identifiers {

    public static String proteinIdentifier(JsonArray jsonArray) {
        String identifier = null;

        for (int i = 0; i < 1; i++) { // One iteration only, because all the elements have the same identifier.
            JsonObject jo = jsonArray.get(i).getAsJsonObject();

            if ( jo.get("protein") != null ) {
                JsonObject protein = jo.get("protein").getAsJsonObject();

                if ( protein.get("id") != null ) {
                    String id = protein.get("id").getAsString();

                    if ( protein.get("uniprots") != null ) {
                        JsonArray uniprots = protein.get("uniprots").getAsJsonArray();

                        for (int j = 0; j < 1; j++) {
                            if ( uniprots.get(i).getAsJsonObject().get("uniprot_acc") != null ) {
                                String uniprotAcc = uniprots.get(i).getAsJsonObject().get("uniprot_acc").getAsString();
                                identifier = id + ";" + uniprotAcc;
                            }
                        }
                    }
                }
            }
        }
        return identifier;
    }

    public static String diseaseIdentifier(JsonArray jsonArray) {
        String identifier = null;

        for (int i = 0; i < 1; i++) {
            String taxonomyId = jsonArray.get(i).getAsJsonObject().get("taxonomy").getAsJsonObject().get("taxonomy_id").getAsString();
            JsonArray diseases = jsonArray.get(i).getAsJsonObject().get("diseases").getAsJsonArray();
            identifier = taxonomyId+";"+diseases.get(i).getAsJsonObject().get("name").getAsString();
        }
        return identifier.replace("\n","");
    }

    public static String sourceIdentifier(JsonArray jsonArray, String query) {
        String identifier = null;
        for (int i = 0; i < 1; i++) {
            String taxonomyId = jsonArray.get(i).getAsJsonObject().get("taxonomy").getAsJsonObject().get("taxonomy_id").getAsString();
            String name = String.valueOf(jsonArray.get(i).getAsJsonObject().get("source").getAsJsonObject().get(query).getAsJsonObject().get("name").getAsString());

            identifier = taxonomyId+";"+name;
        }
        return identifier;
    }

    /*
     * Used to set identifiers for queries without "taxonomy=" in the URL
     * such as references and cell_lines
     */
    public static String noTaxonomyIdentifier(String url, String query) {
        String[] identifier = url.split(query);
        identifier = identifier[1].split("&");
        return identifier[0];
    }
}
