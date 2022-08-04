package org.expasy.glyconnect.doppelganger.QA;

import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.composition;
import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.taxonomy;
import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains functions to analyse the progress of the project step by step.
 * It is use mainly to produce data useful to the dissertation of the thesis and
 * to analyse such data.
 */
public class qualityAnalysis {
    public static void main(String[] args) throws Exception {
        String nLinked = "N-Linked";
        String oLinked = "O-Linked";

        String referencesAll = "referencesAll";
        String proteinsAll = "proteinsAll";
        String diseasesAll = "diseasesAll";
        String cellLinesAll = "cellLinesAll";

        // SourcesAll:
        String cellComponent = "sourcesAll/cell_component";
        String cellType = "sourcesAll/cell_type";
        String tissue = "sourcesAll/tissue";
        String tissuePlant = "sourcesAll/tissue_plant";

        String[] glycanType = {nLinked, oLinked};

        HashMap<String,String[]> overview = new HashMap<>();

        overview.put(proteinsAll, glycanType);
        overview.put(diseasesAll, glycanType);
        overview.put(cellComponent, glycanType);
        overview.put(cellType, glycanType);
        overview.put(tissue, glycanType);
        overview.put(tissuePlant, glycanType);
        overview.put(cellLinesAll, glycanType);
        overview.put(referencesAll, glycanType);

        System.out.println("\nData overview:");
        for (String dir : overview.keySet()) {
            System.out.println(dir);

            for (String type : overview.get(dir)) {
                ArrayList<doppelganger> networks = reader.readfiles(dir, type);

                //jaccardIndexToTable(glycanType, networks, sourceDirectory);

                System.out.println("   " + type);
                dataOverview(networks);
            }
            System.out.println("__________________________________________________");
        }

    }

    public static void dataOverview(ArrayList<doppelganger> networks ) throws Exception {
        ArrayList<String> taxa = new ArrayList<>();
        ArrayList<String> comps = new ArrayList<>();


        for (doppelganger doppelganger: networks) {
            Map<Integer,taxonomy> taxonomies = doppelganger.getGETObject().getTaxonomies();
            Map<Integer, composition> compositions = doppelganger.getGETObject().getCompositions();

            for (int t : taxonomies.keySet()) {
                String taxon = taxonomies.get(t).getCommonName();

                if ( taxon != null && !(taxa.contains(taxon)) ) taxa.add(taxon);
            }

            for (int c : compositions.keySet()) {
                String compo = compositions.get(c).getCondensedFormat();

                if ( compo != null && !(comps.contains(compo)) ) comps.add(compo);
            }
        }

        System.out.println("Entries: "+networks.size()+" - Taxa: "+taxa.size()+" - Compositions: "+comps.size() );
    }


}
