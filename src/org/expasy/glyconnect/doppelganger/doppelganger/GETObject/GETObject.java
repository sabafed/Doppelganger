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
    private final List<reference> references = new ArrayList<>();
    private final List<site> sites = new ArrayList<>();
    private  List<source> sources;
    private final List<structure> structures = new ArrayList<>();
    private final List<taxonomy> taxonomies = new ArrayList<>();

    public int doiless;

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
            JsonArray emptyArray = JsonParser.parseString("[]").getAsJsonArray();
            JsonObject emptyObject = JsonParser.parseString("{}").getAsJsonObject();

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject compositionJson = jsonObject.get("composition").getAsJsonObject();
            composition composition = new composition(compositionJson);
            this.compositions.add(composition);
            //System.out.println("Composition condensed: "+composition.getCondensedFormat());

            JsonArray diseaseJson = emptyArray;
            if ( jsonObject.get("diseases") != null )
                diseaseJson = jsonObject.get("diseases").getAsJsonArray();
            disease disease = new disease(diseaseJson);
            this.diseases.add(disease);
            //System.out.println("Disease : "+disease.getDiseaseJson());

            JsonArray peptideJson = emptyArray;
            if ( jsonObject.get("peptides") != null )
                peptideJson = jsonObject.get("peptides").getAsJsonArray();
            peptide peptide = new peptide(peptideJson);
            this.peptides.add(peptide);
            //System.out.println("Peptide : "+peptide.getPeptideJson());

            JsonArray referenceJson = jsonObject.get("references").getAsJsonArray();
            reference reference = new reference(referenceJson);
            this.references.add(reference);
            doiless += reference.doiless;
            //System.out.println("Reference: "+reference.getReferenceJson());

            JsonObject siteJson = emptyObject;
            if ( jsonObject.get("site") != null )
                siteJson = jsonObject.get("site").getAsJsonObject();
            site site = new site(siteJson);
            this.sites.add(site);
            //System.out.println("Site: "+site.getSiteJson());

            JsonObject sourceJson = jsonObject.get("source").getAsJsonObject();
            System.out.println(sourceJson);

            JsonObject structureJson = jsonObject.get("structure").getAsJsonObject();
            structure structure = new structure(structureJson);
            this.structures.add(structure);
            //System.out.println("Structure: "+structure.getStructureJson());

            JsonObject taxonomyJson = jsonObject.get("taxonomy").getAsJsonObject();
            taxonomy taxonomy = new taxonomy(taxonomyJson);
            this.taxonomies.add(taxonomy);
            //System.out.println("Taxonomy: "+taxonomy.getSpecies());
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
