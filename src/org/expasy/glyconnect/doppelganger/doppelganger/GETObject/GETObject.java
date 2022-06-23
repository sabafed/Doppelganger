package org.expasy.glyconnect.doppelganger.doppelganger.GETObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;


// from this class, call all the other subclasses and make a single object.
public class GETObject {
    private final JsonArray GETSection;
    private final String glycanType;
    private final Map<Integer,composition> compositions = new HashMap<Integer, composition>();
    private final Map<Integer, List<disease>> diseases = new HashMap<Integer, List<disease>>();
    private final Map<Integer, List<peptide>> peptides = new HashMap<Integer, List<peptide>>();
    private final Map<Integer,protein> proteins = new HashMap<Integer, protein>();
    private final Map<Integer, List<reference>> references = new HashMap<Integer, List<reference>>();
    private final Map<Integer,site> sites = new HashMap<Integer, site>();
    private final Map<Integer,source> sources = new HashMap<Integer, source>();
    private final Map<Integer,structure> structures = new HashMap<Integer, structure>();
    private final Map<Integer,taxonomy> taxonomies = new HashMap<Integer, taxonomy>();

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

        // I am using elementIndex to be able to derive the protein database (or other tabs) from the referenceDB
        int elementIndex = 0;
        for (JsonElement jsonElement : GETSection) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            JsonObject compositionJson = jsonObject.get("composition").getAsJsonObject();
            composition composition = new composition(compositionJson);
            this.compositions.put(elementIndex, composition);

            if ( jsonObject.get("diseases") != null ) {
                JsonArray diseaseJson = jsonObject.get("diseases").getAsJsonArray();
                for (JsonElement je : diseaseJson) {
                    disease disease = new disease(je.getAsJsonObject());
                    this.diseases.computeIfAbsent(elementIndex, k -> new ArrayList<disease>());
                    this.diseases.get(elementIndex).add(disease);
                }
            }

            if ( jsonObject.get("peptides") != null ) {
                JsonArray peptideJson = jsonObject.get("peptides").getAsJsonArray();
                for (JsonElement je : peptideJson) {
                    peptide peptide = new peptide(je.getAsJsonObject());
                    this.peptides.computeIfAbsent(elementIndex, k -> new ArrayList<peptide>());
                    this.peptides.get(elementIndex).add(peptide);
                }
            }

            JsonObject proteinJson = jsonObject.get("protein").getAsJsonObject();
            protein protein = new protein(proteinJson);
            this.proteins.put(elementIndex, protein);

            JsonArray referenceJson = jsonObject.get("references").getAsJsonArray();
            for ( JsonElement je : referenceJson ) {
                reference reference = new reference(je.getAsJsonObject());
                this.references.computeIfAbsent(elementIndex, k -> new ArrayList<reference>());
                this.references.get(elementIndex).add(reference);

                doiless += reference.doiless;
            }

            if ( jsonObject.get("site") != null ) {
                JsonObject siteJson = jsonObject.get("site").getAsJsonObject();
                site site = new site(siteJson);
                this.sites.put(elementIndex, site);
            }

            JsonObject sourceJson = jsonObject.get("source").getAsJsonObject();
            source source = new source(sourceJson);
            this.sources.put(elementIndex, source);

            JsonObject structureJson = jsonObject.get("structure").getAsJsonObject();
            structure structure = new structure(structureJson);
            this.structures.put(elementIndex, structure);

            JsonObject taxonomyJson = jsonObject.get("taxonomy").getAsJsonObject();
            taxonomy taxonomy = new taxonomy(taxonomyJson);
            this.taxonomies.put(elementIndex, taxonomy);

            elementIndex++;
        }
    }

    public JsonArray getGETSection() {
        return GETSection;
    }

    public String getGlycanType() {
        return glycanType;
    }

    public Map<Integer, composition> getCompositions() {
        return compositions;
    }

    public Map<Integer, List<disease>> getDiseases() {
        return diseases;
    }

    public Map<Integer, List<peptide>> getPeptides() {
        return peptides;
    }

    public Map<Integer, protein> getProteins() {
        return proteins;
    }

    public Map<Integer, List<reference>> getReferences() {
        return references;
    }

    public Map<Integer, site> getSites() {
        return sites;
    }

    public Map<Integer, source> getSources() {
        return sources;
    }

    public Map<Integer, structure> getStructures() {
        return structures;
    }

    public Map<Integer, taxonomy> getTaxonomies() {
        return taxonomies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GETObject)) return false;
        GETObject getObject = (GETObject) o;
        return Objects.equals(glycanType, getObject.glycanType) &&
                Objects.equals(compositions, getObject.compositions) &&
                Objects.equals(diseases, getObject.diseases) &&
                Objects.equals(peptides, getObject.peptides) &&
                Objects.equals(proteins, getObject.proteins) &&
                Objects.equals(references, getObject.references) &&
                Objects.equals(sites, getObject.sites) &&
                Objects.equals(sources, getObject.sources) &&
                Objects.equals(structures, getObject.structures) &&
                Objects.equals(taxonomies, getObject.taxonomies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(glycanType,
                compositions,
                diseases,
                peptides,
                proteins,
                references,
                sites,
                sources,
                structures,
                taxonomies);
    }

    @Override
    public String toString() {
        return String.valueOf(this.GETSection);
    }

    public void attributesChecker() {
        System.out.println(
                "Compositions: " + this.getCompositions()+"\n"+
                "Diseases:     "+this.getDiseases()+"\n"+
                "Peptides:     "+this.getPeptides()+"\n"+
                "Proteins:     "+this.getProteins()+"\n"+
                "References:   "+this.getReferences()+"\n"+
                "Sites:        "+this.getSites()+"\n"+
                "Sources:      "+this.getSources()+"\n"+
                "Structures:   "+this.getStructures()+"\n"+
                "Taxonomies:   "+this.getTaxonomies()+"\n");
    }
}
