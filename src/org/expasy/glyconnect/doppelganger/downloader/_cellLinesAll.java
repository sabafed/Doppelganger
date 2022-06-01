package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.PrintStream;
import java.net.URLEncoder;

public class _cellLinesAll {
    public static void main(String[] args) throws Exception {
        cellLinesAllDownloader();
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

                if ( nGET.getResponse().length() > 2 && nGET.getResponse() != null ) cellLineToJson(nGET);
                if ( oGET.getResponse().length() > 2 && oGET.getResponse() != null ) cellLineToJson(oGET);
            }
        }
    }

    public static void cellLineToJson(GETRequest getObj) throws Exception {
        System.out.println("________________________________________________\nSending POSTRequest");
        POSTRequest postObj = new POSTRequest(getObj);

        String GETBody = postObj.getGETBody();
        GETBody = GETBody.substring(0, GETBody.length()-1);

        String POSTBody = postObj.getPOSTBody();
        POSTBody = POSTBody.substring(1);

        String output = GETBody+","+POSTBody;

        if ( postObj.getIdentifier() != null ) {
            String outFileName = "/home/federico/Documenti/Thesis/Doppelganger/cellLinesAll/" +
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
