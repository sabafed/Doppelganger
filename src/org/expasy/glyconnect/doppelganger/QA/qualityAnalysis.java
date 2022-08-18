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
        //dataOverview(overviewMap);
        String proteinsAll = "proteinsAll";

        String nLinked = "N-Linked";
        String oLinked = "O-Linked";
        String[] types = new String[]{nLinked,oLinked};

        HashMap<String,String[]> methods = methodsMap();
        double[] thresholds = thresholds();

        for (String type : types) {
            //ArrayList<doppelganger> doppelgangers = reader.readfiles(proteinsAll, type);

            for (String method : methods.keySet()) {
                //QAExport.methodToTable(type, doppelgangers, proteinsAll, method);

                for (String score : methods.get(method)) {
                    //if ( method.equals("density") ) threshold = 1.0;

                    for (double threshold : thresholds) {
                        QAExport.QAExport(proteinsAll, type, method, score, threshold);
                    }
                }
            }
        }
    }

    public static void dataOverview(HashMap<String,String[]> overview) throws Exception {
        System.out.println("\nData overview:");

        for (String dir : overview.keySet()) {
            System.out.println(dir);

            for (String type : overview.get(dir)) {
                ArrayList<doppelganger> networks = reader.readfiles(dir, type);
                System.out.println("   " + type);

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

            System.out.println("__________________________________________________");
        }
    }

    public static HashMap<String,String[]> methodsMap() {
        HashMap<String,String[]> methods = new HashMap<>();

        String linkCosVT = "Link Cosine Similarity (virtual T)";
        String linkCosVF = "Link Cosine Similarity (virtual F)";

        String profCosVT = "Profile Cosine Similarity (virtual T)";
        String profCosVF = "Profile Cosine Similarity (virtual F)";

        String realNodesJI = "Real Nodes Jaccard Index";
        String realLinksJI = "Real Links Jaccard Index";

        String virtualNodesJI = "Virtual Nodes Jaccard Index";
        String virtualLinksJI = "Virtual Links Jaccard Index";

        String densityRatioVT = "Density Ratio (Virtual T)";
        String densityRatioVF = "Density Ratio (Virtual F)";

        methods.put("linkCosSim", new String[]{linkCosVT, linkCosVF});
        methods.put("profileCosSim", new String[]{profCosVT, profCosVF});
        //methods.put("density", new String[]{densityRatioVT, densityRatioVF});
        methods.put("JaccardIndex", new String[]{realNodesJI, realLinksJI, virtualNodesJI, virtualLinksJI});

        return methods;
    }

    public static double[] thresholds() {
        return new double[]{0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95};
    }
    public static HashMap<String,String[]> overviewMap() {
        HashMap<String,String[]> overview = new HashMap<>();

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

        overview.put(proteinsAll, glycanType);
        overview.put(diseasesAll, glycanType);
        overview.put(cellComponent, glycanType);
        overview.put(cellType, glycanType);
        overview.put(tissue, glycanType);
        overview.put(tissuePlant, glycanType);
        overview.put(cellLinesAll, glycanType);
        overview.put(referencesAll, glycanType);

        return overview;
    }

}
