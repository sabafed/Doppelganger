package org.expasy.glyconnect.doppelganger.downloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 *  Class that sends a GET request to https://beta.glyconnect.expasy.org/api/
 */
public class GETRequest {
    private final String url;
    private String responseGET;

    /**
     * Main constructor
     *
     * @param url The url address to query the api.
     */
    public GETRequest(String url) throws Exception {
        this.url = url;
        this.setResponse();
    }

    public String sendGET() throws Exception {
        URL urlObj = new URL(this.url);

        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        Integer responseCode = connection.getResponseCode();
/*
        if ( responseCode != 200 ){
            System.out.println("WARNING: GET Request Response Code = "+ responseCode);
            System.exit(1);
        }
*/
        StringBuffer response = new StringBuffer();

        if ( responseCode == HttpURLConnection.HTTP_OK )
        {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = inputReader.readLine()) != null) response.append(inputLine);

            inputReader.close();
        }

        return response.toString();
    }

    public void setResponse() throws Exception {
        String response = this.sendGET();

        if ( response.length() > 2 && response.contains(",\"results\"") ) {
            int startIndex = response.indexOf(",\"results\"") + 1;
            int endIndex   = response.indexOf("}]}]}");
            response = response.substring(startIndex,endIndex)+"}]}]";
        }
        this.responseGET = response; // if empty, response == "[]"
    }

    public String getUrl() {
        return url;
    }

    public String getResponse() {
        return responseGET;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GETRequest)) return false;
        GETRequest that = (GETRequest) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(responseGET, that.responseGET);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, responseGET);
    }

    @Override
    public String toString() {
        return "GETRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
