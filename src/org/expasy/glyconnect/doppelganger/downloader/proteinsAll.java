package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.net.URLEncoder;

public class proteinsAll {
    public static void main(String[] args) throws Exception {
        proteinsAllDownloader();
    }
    public static void proteinsAllDownloader() throws Exception {
        GETRequest proteinsAll = new GETRequest("https://glyconnect.expasy.org/api/proteins-all");
        String json = proteinsAll.getResponse();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);

            String species = "";
            String protein = "";

            if (jsonObject.get("taxonomy") != null) {
                JsonObject taxonomy = jsonObject.get("taxonomy").getAsJsonObject();
                if ( taxonomy.get("species") != null ) {
                    species = URLEncoder.encode(taxonomy.get("species").getAsString(), "UTF-8");
                }
            }

            if ( jsonObject.get("name") != null && !jsonObject.get("name").isJsonNull() )
                protein = URLEncoder.encode(jsonObject.get("name").getAsString(), "UTF-8");

            String nUrl = "https://glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&protein="+protein+"&glycan_type=N-Linked";
            String oUrl = "https://glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&protein="+protein+"&glycan_type=O-Linked";

            GETRequest nGET = new GETRequest(nUrl);
            GETRequest oGET = new GETRequest(oUrl);

            if (nGET.getResponse().length() > 2) proteinToJson(nGET);
            if (oGET.getResponse().length() > 2) proteinToJson(oGET);
        }
    }

    public static void proteinToJson(GETRequest getObj) throws Exception {
        System.out.println("________________________________________________\nSending POSTRequest");
        POSTRequest postObj = new POSTRequest(getObj);

        String GETBody = postObj.getGETBody();
        GETBody = GETBody.substring(0, GETBody.length()-1);

        String POSTBody = postObj.getPOSTBody();
        POSTBody = POSTBody.substring(1);

        String output = GETBody+","+POSTBody;

        if ( postObj.getIdentifier() != null ) {
            String outFileName = "/home/federico/Documenti/Thesis/Doppelganger/proteinsAll/" +
                    postObj.getGlycanType() + "/" +
                        postObj.getIdentifier();

            PrintStream outFile = new PrintStream(outFileName);
            PrintStream console = System.out;
            System.setOut(outFile);
            System.out.println(output);
            System.setOut(console);
            System.out.println("    File '"+outFileName+"' created!");
        }
    }
}
