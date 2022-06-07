package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class downloader {
    public static void main(String[] args) throws Exception {
        //proteinsAllDownloader();
        //diseasesAllDownloader();
        //cellLinesAllDownloader();
        //referencesAllDownloader();
    }
    public static void proteinsAllDownloader() throws Exception {
        GETRequest proteinsAll = new GETRequest("https://beta.glyconnect.expasy.org/api/proteins-all");
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

            String nUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&protein="+protein+"&glycan_type=N-Linked";
            String oUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&protein="+protein+"&glycan_type=O-Linked";

            GETRequest nGET = new GETRequest(nUrl);
            GETRequest oGET = new GETRequest(oUrl);

            if (nGET.getResponse().length() > 2) dataToJson(nGET, "proteinsAll");
            if (oGET.getResponse().length() > 2) dataToJson(oGET, "proteinsAll");
        }
    }

    public static void diseasesAllDownloader() throws Exception {
        GETRequest diseasesAll = new GETRequest("https://beta.glyconnect.expasy.org/api/diseases-all");
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
                        String nUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&disease="+ disease +"&glycan_type=N-Linked";
                        String oUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?taxonomy="+species+"&disease="+ disease +"&glycan_type=O-Linked";

                        GETRequest nGET = new GETRequest(nUrl);
                        GETRequest oGET = new GETRequest(oUrl);

                        if ( nGET.getResponse().length() > 2 && nGET.getResponse() != null ) dataToJson(nGET, "diseasesAll");
                        if ( oGET.getResponse().length() > 2 && oGET.getResponse() != null ) dataToJson(oGET, "diseasesAll");
                    }
                }
            }
        }
    }

    public static void cellLinesAllDownloader() throws Exception {
        GETRequest cellLinesAll = new GETRequest("https://beta.glyconnect.expasy.org/api/cell_lines-all");
        String json = cellLinesAll.getResponse();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            String cellLine = "";

            if ( jsonObject.get("name") != null && !jsonObject.get("name").isJsonNull() ) {
                cellLine = URLEncoder.encode(jsonObject.get("name").getAsString(), "UTF-8");

                String nUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?cell_line="+ cellLine +"&glycan_type=N-Linked";
                String oUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?cell_line="+ cellLine +"&glycan_type=O-Linked";

                GETRequest nGET = new GETRequest(nUrl);
                GETRequest oGET = new GETRequest(oUrl);

                if ( nGET.getResponse().length() > 2 && nGET.getResponse() != null ) dataToJson(nGET, "cellLinesAll");
                if ( oGET.getResponse().length() > 2 && oGET.getResponse() != null ) dataToJson(oGET, "cellLinesAll");
            }
        }
    }

    public static void referencesAllDownloader() throws Exception {
        GETRequest referencesAll = new GETRequest("https://beta.glyconnect.expasy.org/api/references/all");
        String json = referencesAll.getResponse();
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        List<String> referenceDoi = new ArrayList<>();
        List<String> doiless = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject reference = (JsonObject)jsonArray.get(i);
            if (reference.get("doi") != null) referenceDoi.add(reference.get("doi").getAsString());
            else doiless.add(reference.get("title").getAsString());
        }

        for (String doi : referenceDoi) {
            String nUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?reference="+doi+"&glycan_type=N-Linked";
            String oUrl = "https://beta.glyconnect.expasy.org/api/glycosylations?reference="+doi+"&glycan_type=O-Linked";

            GETRequest nGET = new GETRequest(nUrl);
            GETRequest oGET =  new GETRequest(oUrl);

            if (nGET.getResponse().length() > 2) dataToJson(nGET, "referencesAll");
            if (oGET.getResponse().length() > 2) dataToJson(oGET, "referencesAll");
        }
    }

    public static void dataToJson(GETRequest getObj, String targetDirectory) throws Exception {
        System.out.println("________________________________________________\nSending POSTRequest");
        POSTRequest postObj = new POSTRequest(getObj);

        String GETBody = postObj.getGETBody();
        GETBody = GETBody.substring(0, GETBody.length()-1);

        String POSTBody = postObj.getPOSTBody();
        POSTBody = POSTBody.substring(1);

        String output = GETBody+","+POSTBody;

        if ( postObj.getIdentifier() != null ) {
            String outFileName = "/home/federico/Documenti/Thesis/Doppelganger/"+targetDirectory+"/" +
                    postObj.getGlycanType() + "/" +
                        postObj.getIdentifier().replace("/","_"); // "/" is folder separator.

            PrintStream outFile = new PrintStream(outFileName);
            PrintStream console = System.out;
            System.setOut(outFile);
            System.out.println(output);
            System.setOut(console);
            System.out.println("    File '"+outFileName+"' created!");
        }
    }
}