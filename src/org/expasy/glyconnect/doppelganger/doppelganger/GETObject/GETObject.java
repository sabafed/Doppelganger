package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

// from this class, call all the other subclasses and make a single object.
public class GETObject {
    private final String glycanType;
    private final List<composition> compositions = new ArrayList<>();
    private final List<disease> diseases = new ArrayList<>();
    private final List<peptide> peptides = new ArrayList<>();
    private  List<protein> proteins;
    private  List<reference> references;
    private  List<site> sites;
    private  List<source> sources;
    private  List<structure> structures;
    private  List<taxonomy> taxonomies;

    /**
     *  Main constructor
     *
     * @param GETSection The GET response in json format.
     * @param glycanType N-Linked or O-Linked, inherited from class doppelganger.
     */
    public GETObject(JsonArray GETSection, String glycanType) {
        this.glycanType = glycanType;

        int count = 0;
        for (JsonElement jsonElement : GETSection) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject compositionJson = jsonObject.get("composition").getAsJsonObject();
            composition composition = new composition(compositionJson);
            this.compositions.add(composition);
            //System.out.println("Composition condensed: "+composition.getCondensedFormat());

            JsonArray emptyArray = JsonParser.parseString("[]").getAsJsonArray();

            JsonArray diseaseJson = emptyArray;
            if ( jsonObject.get("diseases") != null )
                diseaseJson = jsonObject.get("diseases").getAsJsonArray();
            disease disease = new disease(diseaseJson);
            this.diseases.add(disease);
            //System.out.println("Disease id: "+disease.getDiseaseJson());

            JsonArray peptideJson = emptyArray;
            if ( jsonObject.get("peptides") != null )
                peptideJson = jsonObject.get("peptides").getAsJsonArray();
            peptide peptide = new peptide(peptideJson);
            this.peptides.add(peptide);
            //System.out.println("Peptide id: "+peptide.getId());
        }

        /* Maybe set up a method printCompositions() to print the array?
        System.out.print("[");
        for (composition compo : this.compositions) System.out.print(compo.toString()+", ");
        System.out.println("]");
        */
    }
    public String getGlycanType() {
        return glycanType;
    }

    public List<composition> getCompositions() {
        return compositions;
    }

    public List<disease> getDiseases() {
        return diseases;
    }

    public List<peptide> getPeptides() {
        return peptides;
    }

    public List<protein> getProteins() {
        return proteins;
    }

    public List<reference> getReferences() {
        return references;
    }

    public List<site> getSites() {
        return sites;
    }

    public List<source> getSources() {
        return sources;
    }

    public List<structure> getStructures() {
        return structures;
    }

    public List<taxonomy> getTaxonomies() {
        return taxonomies;
    }
}
