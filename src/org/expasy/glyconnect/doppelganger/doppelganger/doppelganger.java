package org.expasy.glyconnect.doppelganger.doppelganger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.GETObject;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.POSTObject;

import java.nio.file.Files;
import java.nio.file.Path;

// from this class, call all the others and make a single object.
public class doppelganger {
    private final String doi;

    private String glycanType;

    private final GETObject GETSection;
    private final POSTObject POSTSection;

    public int doiless;
    /**
     * Main constructor
     *
     * @param doiJson Path of the json file to process saved with DOI as its file name.
     */
    public doppelganger(Path doiJson) throws Exception {
        this.doi = doiJson.getFileName().toString().replace("_","/");
        this.setGlycanType(doiJson);

        String jsonContent = Files.readString((doiJson));

        JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

        JsonArray GETSection = jsonObject.getAsJsonArray("GETRequest");
        JsonObject POSTSection = jsonObject.getAsJsonObject("POSTRequest");

        this.GETSection = new GETObject(GETSection, this.glycanType);

        this.doiless = this.GETSection.doiless;
        this.POSTSection = new POSTObject(POSTSection);

    }

    public void setGlycanType(Path doiJson) {
        String path = doiJson.toString();

        if ( path.contains("N-Linked") ) this.glycanType = "N-Linked";
        else if ( path.contains("O-Linked") ) this.glycanType = "O-Linked";
        else this.glycanType = "Unknown";
    }

    public String getDoi() {
        return doi;
    }

    public GETObject getGETSection() {
        return GETSection;
    }

    public POSTObject getPOSTSection() {
        return POSTSection;
    }

    public String getGlycanType() {
        return glycanType;
    }

}
