package org.expasy.glyconnect.doppelganger.doppelganger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.GETObject;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.POSTObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// from this class, call all the others and make a single object.
public class doppelganger {
    private String identifier;
    private String doi;
    private String glycanType;
    private final GETObject GETObject;
    private final POSTObject POSTObject;
    public int doiless;

    /**
     * Main constructor
     *
     * @param sourceJson Path of the json file to process. Depending on the file, the file name can be:
     *                   - the DOI under which the network is referenced in Glyconnect;
     *                   - the identifier composed as "GlyconnectId;UniprotAcc".
     */
    public doppelganger(Path sourceJson) throws Exception {
        if ( sourceJson.toString().contains("proteinsAll") )
            this.identifier = sourceJson.getFileName().toString();

        if ( sourceJson.toString().contains("referencesAll") )
            this.doi = sourceJson.getFileName().toString().replace("_","/");
        this.setGlycanType(sourceJson);

        String jsonContent = Files.readString((sourceJson));

        JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

        JsonArray GETSection = jsonObject.getAsJsonArray("GETRequest");
        JsonObject POSTSection = jsonObject.getAsJsonObject("POSTRequest");

        this.GETObject = new GETObject(GETSection, this.glycanType);
        this.doiless = this.GETObject.doiless;

        this.POSTObject = new POSTObject(POSTSection);
    }

    public String getIdentifier() {
        return identifier;
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

    public GETObject getGETObject() {
        return GETObject;
    }

    public POSTObject getPOSTObject() {
        return POSTObject;
    }

    public String getGlycanType() {
        return glycanType;
    }

    public boolean equals(doppelganger other) {
        if ( (this.doi != null && other.doi != null && this.doi.equals(other.doi)) ||
                (this.identifier != null && other.identifier != null && this.identifier.equals(other.identifier)) ) {
            if ( this.glycanType.equals(other.glycanType) ) {
                if ( this.getGETObject().equals(other.getGETObject()) ) {
                    if ( this.getPOSTObject().equals(other.getPOSTObject()) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInside(List<doppelganger> list) {
        for (doppelganger doppel : list) {
            if ( this.equals(doppel) ) return true;
        }
        return false;
    }

    public void attributesChecker() {
        System.out.println(this.getDoi()+"\n"+
                this.getIdentifier()+"\n"+
                this.getGlycanType()+"\n"+
                this.getGETObject()+"\n"+
                this.getPOSTObject()+"\n");
    }
}
