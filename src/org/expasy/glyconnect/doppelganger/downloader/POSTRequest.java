package org.expasy.glyconnect.doppelganger.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class POSTRequest {
    private String url;
    private String GETBody;
    private String POSTBody;
    private String doi;
    private String identifier;
    private String taxonomy;
    private String protein;
    private String glycanType;

    /**
     * Main constructor
     */
    public POSTRequest(GETRequest GETReq) throws Exception {
        this.url = GETReq.getUrl();
        this.setGETBody(GETReq);
        this.setPOSTBody(GETReq);
        if ( this.url.contains("reference=") ) this.setDoi();
        if ( this.url.contains("taxonomy=") ) this.setTaxonomy();
        if ( this.url.contains("protein=") ) {
            this.setProtein();
            this.setIdentifier();
        }
        this.setGlycanType();
    }

    public String sendPOST(GETRequest GETReq) throws Exception {
        StringBuilder response = new StringBuilder();

        // Server's URL:
        URL urlObj = new URL("https://glyconnect.expasy.org/compozitor/api/query");

        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("POST");

        // Set the “content-type” request header to “application/json” to send the request content in JSON form
        connection.setRequestProperty("Content-Type", "application/json");

        // Set the “Accept” request header to “application/json” to read the response in the desired format
        connection.setRequestProperty("Accept", "application/json");

        // Enable the URLConnection object's doOutput property to true in order to send request content
        // Otherwise, it won't be possible to write content to the connection output stream
        connection.setDoOutput(true);

        // Composing the body of the request
        String s1 = "{ includeVirtual :true, query :[{";
        String s2 = ", select : Organism|Protein|X-Linked|AA-000 , hash :0}]}";
        s1 = s1.replace(' ', '"');
        s2 = s2.replace(' ', '"');
        String inputString = s1+ GETReq.getResponse()+s2;

        try(OutputStream out = connection.getOutputStream())
        {
            byte[] in = inputString.getBytes("utf-8");
            out.write(in, 0, in.length);
        }

        // Read the Response From Input Stream
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")))
        {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null)
            {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    public void setGETBody(GETRequest GETReq) {
        String GETBody = GETReq.getResponse();

        if (GETBody.length() > 2) {
            int startIndex = GETBody.indexOf(",\"results\"") + 11;
            int endIndex   = GETBody.indexOf("}]}]");
            GETBody = "{\"GETRequest\":"+GETBody.substring(startIndex,endIndex)+"}]}]}";
        }
        this.GETBody = GETBody;
    }

    public void setPOSTBody(GETRequest GETReq) throws Exception {
        String POSTResponse = sendPOST(GETReq);
        String POSTBody = "{\"POSTRequest\":"+POSTResponse+"}";

        this.POSTBody = POSTBody;
    }

    public void setDoi() {
        String[] doi = this.url.split("reference=");
        doi = doi[1].split("&");
        this.doi = doi[0];
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier() {
        JsonObject jsonObject = JsonParser.parseString(this.GETBody).getAsJsonObject();

        JsonArray jsonArray = jsonObject.get("GETRequest").getAsJsonArray();

        for (int i = 0; i < 1; i++) {
            JsonObject jo = jsonArray.get(i).getAsJsonObject();

            if ( jo.get("protein") != null ) {
                JsonObject protein = jo.get("protein").getAsJsonObject();

                if ( protein.get("id") != null ) {
                    String id = protein.get("id").getAsString();

                    if ( protein.get("uniprots") != null ) {
                        JsonArray uniprots = protein.get("uniprots").getAsJsonArray();

                        for (int j = 0; j < 1; j++) {
                            if ( uniprots.get(i).getAsJsonObject().get("uniprot_acc") != null) {
                                String uniprotAcc = uniprots.get(i).getAsJsonObject().get("uniprot_acc").getAsString();
                                this.identifier = id+";"+uniprotAcc;
                            }
                        }
                    }
                }
            }
        }
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy() {
        String[] taxonomy = this.url.split("taxonomy=");
        taxonomy = taxonomy[1].split("&");
        this.taxonomy = taxonomy[0];
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein() {
        String[] protein = this.url.split("protein=");
        protein = protein[1].split("&");
        this.protein = protein[0];
    }
    public void setGlycanType() {
        if (this.url.contains("N-Linked")) this.glycanType = "N-Linked";
        else if (this.url.contains("O-Linked")) this.glycanType = "O-Linked";
        else this.glycanType = "null";
    }

    public String getDoi() {
        return doi;
    }

    public String getGlycanType() {
        return glycanType;
    }

    public String getPOSTBody() {
        return POSTBody;
    }

    public String getGETBody() {
        return GETBody;
    }
}
