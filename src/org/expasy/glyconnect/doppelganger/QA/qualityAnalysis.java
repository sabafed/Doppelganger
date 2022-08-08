package org.expasy.glyconnect.doppelganger.QA;

import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.composition;
import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.taxonomy;
import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

        //dataOverview(overview);
        //importQAComparisons(nLinked,proteinsAll,"Wanted");
        //importResultsTable(proteinsAll,nLinked,"JaccardIndex");

        statistics();
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

    public static ArrayList<String> importQAComparisons(String type, String dataset, String wanted) throws Exception {
        ArrayList<String> comps = new ArrayList<>();

        String compareTo = type+"_"+dataset.replace("All","List")+wanted+".tsv";

        File compareFile = new File("/home/federico/Documenti/Thesis/Doppelganger/dataAll/misc/QA/"+compareTo);

        FileReader importFR = new FileReader(compareFile);
        BufferedReader importBR = new BufferedReader(importFR);

        String line;

        while ( (line = importBR.readLine()) != null ) {
            String[] comparison = line.split("\\t");
            String comp = comparison[0]+"~"+comparison[1];

            if ( !(comps.contains(comp)) ) comps.add(comp);
        }

        return comps;
    }

    public static HashMap<String,String> importResultsTable(String dataset, String type, String method) throws Exception {
        //{identifierA~identifierB, nodesScore~linksScore}
        HashMap<String,String> results = new HashMap<>();

        String compareTo = dataset+"_"+type+"_minSize5_"+method+".tsv";

        File compareFile = new File("/home/federico/Documenti/Thesis/Doppelganger/results/steps/"+compareTo);

        FileReader importFR = new FileReader(compareFile);
        BufferedReader importBR = new BufferedReader(importFR);

        String line = importBR.readLine(); // Skipping header row

        while ( (line = importBR.readLine()) != null ) {
            String[] comparison = line.split("\\t");
            String comp = comparison[0]+"~"+comparison[1];
            String nodesLinksVals = comparison[10]+"~"+comparison[13];
            results.computeIfAbsent(comp, k -> nodesLinksVals);
        }

        return results;
    }

    public static void statistics() throws Exception {
        String dataset = "proteinsAll";
        String type = "N-Linked";
        String method = "JaccardIndex";
        String wanted = "Unwanted";

        HashMap<String,String> results = importResultsTable(dataset, type, method);

        ArrayList<String> comps = importQAComparisons(type, dataset, wanted );

        int counter = 0;

        for (String res : results.keySet()) {
            for (String comp : comps) {
                if ( res.equals(comp) ) {
                    counter++;
                    System.out.println(counter+" - " + wanted + " comparison: " + comp +
                            "\nretrieved with method: " + method +
                            "\nscores: " + results.get(res) + "\n");
                }
            }
        }

        System.out.println("Total "+wanted+" comparisons: "+ comps.size()+
                "\nComparisons retrieved: "+counter+
                "\nComparisons missed: "+(comps.size()-counter));
    }
}
