package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

// from this class, call all the other subclasses and make a single object.
public class GETObject {
    private final JsonArray GETSection;
    private final String glycanType;
    private final List<composition> compositions = new ArrayList<>();
    private final List<disease> diseases = new ArrayList<>();
    private final List<peptide> peptides = new ArrayList<>();
    private final List<protein> proteins = new ArrayList<>();
    private final List<reference> references = new ArrayList<>();
    private final List<site> sites = new ArrayList<>();
    private final List<source> sources = new ArrayList<>();
    private final List<structure> structures = new ArrayList<>();
    private final List<taxonomy> taxonomies = new ArrayList<>();

    public int doiless;

    /**     A WAY TO AVOID OBJECT REPETITIONS WILL HAVE TO BE IMPLEMENTED
     *      https://stackoverflow.com/questions/369512/how-to-compare-objects-by-multiple-fields/20093642#20093642
     *  Main constructor
     *
     * @param GETSection The GET response in json format.
     * @param glycanType N-Linked or O-Linked, inherited from class doppelganger.
     */
    public GETObject(JsonArray GETSection, String glycanType) {
        this.GETSection = GETSection;
        this.glycanType = glycanType;

        for (JsonElement jsonElement : GETSection) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject compositionJson = jsonObject.get("composition").getAsJsonObject();
            composition composition = new composition(compositionJson);
            this.compositions.add(composition);

            if ( jsonObject.get("diseases") != null ) {
                JsonArray diseaseJson = jsonObject.get("diseases").getAsJsonArray();
                for (JsonElement je : diseaseJson) {
                    disease disease = new disease(je.getAsJsonObject());
                    this.diseases.add(disease);
                }
            }

            if ( jsonObject.get("peptides") != null ) {
                JsonArray peptideJson = jsonObject.get("peptides").getAsJsonArray();
                for (JsonElement je : peptideJson) {
                    peptide peptide = new peptide(je.getAsJsonObject());
                    this.peptides.add(peptide);
                }
            }

            JsonObject proteinJson = jsonObject.get("protein").getAsJsonObject();
            protein protein = new protein(proteinJson);
            this.proteins.add(protein);

            JsonArray referenceJson = jsonObject.get("references").getAsJsonArray();
            for ( JsonElement je : referenceJson ) {
                reference reference = new reference(je.getAsJsonObject());
                this.references.add(reference);

                doiless += reference.doiless;
            }

            if ( jsonObject.get("site") != null ) {
                JsonObject siteJson = jsonObject.get("site").getAsJsonObject();
                site site = new site(siteJson);
                this.sites.add(site);
            }

            JsonObject sourceJson = jsonObject.get("source").getAsJsonObject();
            source source = new source(sourceJson);
            this.sources.add(source);

            JsonObject structureJson = jsonObject.get("structure").getAsJsonObject();
            structure structure = new structure(structureJson);
            this.structures.add(structure);

            JsonObject taxonomyJson = jsonObject.get("taxonomy").getAsJsonObject();
            taxonomy taxonomy = new taxonomy(taxonomyJson);
            this.taxonomies.add(taxonomy);
        }
    }

    public JsonArray getGETSection() {
        return GETSection;
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

    @Override
    public String toString() {
        return this.GETSection.getAsString();
    }

    public boolean equals(GETObject getObject){
        if (this.getGETSection().equals(getObject.getGETSection())) return true;
        return false;
    }
}
