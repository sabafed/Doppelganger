package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.net.URLEncoder;

public class diseaseAll {
    public static void main(String[] args) throws Exception {
        diseasesAllDownloader();
    }
    public static void diseasesAllDownloader() throws Exception {
        GETRequest diseasesAll = new GETRequest("https://glyconnect.expasy.org/api/diseases-all");
        String json = diseasesAll.getResponse();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            String disease = "";

            if ( jsonObject.get("name") != null && !jsonObject.get("name").isJsonNull() )
                disease = URLEncoder.encode(jsonObject.get("name").getAsString(), "UTF-8");

            if ( jsonObject.get("taxons") != null ) {
                JsonArray taxons = jsonObject.get("taxons").getAsJsonArray();
                for (JsonElement je : taxons) {
                    String species = "";

                    JsonObject jo = je.getAsJsonObject();
                    if ( jo.get("species") != null ) {
                        species = URLEncoder.encode(jo.get("species").getAsString(), "UTF-8");
                        String nUrl = "https://glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&disease="+ disease +"&glycan_type=N-Linked";
                        String oUrl = "https://glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&disease="+ disease +"&glycan_type=O-Linked";

                        GETRequest nGET = new GETRequest(nUrl);
                        GETRequest oGET = new GETRequest(oUrl);

                        if ( nGET.getResponse().length() > 2 && nGET.getResponse() != null ) diseaseToJson(nGET);
                        if ( oGET.getResponse().length() > 2 && oGET.getResponse() != null ) diseaseToJson(oGET);
                    }
                }
            }
        }
    }

    public static void diseaseToJson(GETRequest getObj) throws Exception {
        System.out.println("________________________________________________\nSending POSTTRequest");
        POSTRequest postObj = new POSTRequest(getObj);

        String GETBody = postObj.getGETBody();
        GETBody = GETBody.substring(0, GETBody.length()-1);

        String POSTBody = postObj.getPOSTBody();
        POSTBody = POSTBody.substring(1);

        String output = GETBody+","+POSTBody;

        if ( postObj.getIdentifier() != null ) {
            String outFileName = "/home/federico/Documenti/Thesis/Doppelganger/diseasesAll/" +
                    postObj.getGlycanType() + "/" +
                    postObj.getIdentifier().replace("/","_");

            PrintStream outFile = new PrintStream(outFileName);
            PrintStream console = System.out;
            System.setOut(outFile);
            System.out.println(output);
            System.setOut(console);
            System.out.println("    File '"+outFileName+"' created!");
        }
    }
}
