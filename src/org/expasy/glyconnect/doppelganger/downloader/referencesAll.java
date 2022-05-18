package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class referencesAll {
    public static void main(String[] args) throws Exception {
        //String url = "https://glyconnect.expasy.org/api/glycosylations?reference=10.1016/j.talanta.2020.121495&glycan_type=N-Linked";
        //referenceDBdownloader();
    }

    public static void referenceDBdownloader() throws Exception {
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
            String nUrl = "https://glyconnect.expasy.org/api/glycosylations?reference="+doi+"&glycan_type=N-Linked";
            String oUrl = "https://glyconnect.expasy.org/api/glycosylations?reference="+doi+"&glycan_type=O-Linked";

            GETRequest nGET = new GETRequest(nUrl);
            GETRequest oGET =  new GETRequest(oUrl);

            if (nGET.getResponse().length() > 2) referenceToJson(nGET);
            if (oGET.getResponse().length() > 2) referenceToJson(oGET);
        }
    }

    public static void referenceToJson(GETRequest getObj) throws Exception {
        POSTRequest postObj = new POSTRequest(getObj);

        String GETBody = postObj.getGETBody();
        GETBody = GETBody.substring(0, GETBody.length()-1);

        String POSTBody = postObj.getPOSTBody();
        POSTBody = POSTBody.substring(1);

        String output = GETBody+","+POSTBody;
        String outFileName =
                "/home/federico/Documenti/Thesis/Doppelganger/referenceDB/"+
                        postObj.getGlycanType()+"/"+
                        postObj.getDoi().replace("/","_");// "/" is folder separator.

        PrintStream outFile = new PrintStream(outFileName);
        PrintStream console = System.out;
        System.setOut(outFile);
        System.out.println(output);
        System.setOut(console);
        System.out.println("    File '"+outFileName+"' created!");
    }
}
