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

        /*
        QAExport.jaccardIndexToTable(
                nLinked,
                reader.readfiles(proteinsAll,nLinked),
                proteinsAll);
        */

        //dataOverview(overview);
        //importQAComparisons(nLinked,proteinsAll,"Wanted");
        //importResultsTable(proteinsAll,nLinked,"JaccardIndex");



        //ArrayList<doppelganger> doppelgangers = reader.readfiles(proteinsAll,nLinked);

        //QAExport.jaccardIndexToTable(nLinked,doppelgangers,proteinsAll,"jaccardIndex");

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



    public static void statistics() throws Exception {
        String dataset = "proteinsAll";
        String method = "jaccardIndex";

        HashMap<String,String> nResults = QAImport.importResultsTable(dataset, "N-Linked", method);
        //HashMap<String,String> oResults = QAImport.importResultsTable(dataset, "O-Linked", method);

        ArrayList<String> nCompsWanted = QAImport.importQAComparisons("N-Linked", dataset, "Wanted");
        ArrayList<String> nCompsUnwanted = QAImport.importQAComparisons("N-Linked", dataset, "Unwanted");

        //ArrayList<String> oCompsWanted = QAImport.importQAComparisons("O-Linked", dataset, "Wanted");
        //ArrayList<String> oCompsUnwanted = QAImport.importQAComparisons("O-Linked", dataset, "Unwanted");

        int nWantedCount = 0;
        int nUnwantedCount = 0;

        ArrayList<String> nWantedFound =  new ArrayList<>();
        ArrayList<String> nUnwantedFound = new ArrayList<>();

        for (String res : nResults.keySet()) {
            for (String comp : nCompsWanted) {
                if ( comp.equals(res) ) {
                    if ( !(nWantedFound.contains(comp)) ) nWantedFound.add(comp);

                    nWantedCount++;
                    System.out.println(nWantedCount +" - wanted comparison: " + comp +
                            "\nretrieved with method: " + method +
                            "\nscores: " + nResults.get(res) + "\n");
                }
            }
        }

        for (String res : nResults.keySet()) {
            for (String comp : nCompsUnwanted) {
                if (res.equals(comp)) {
                    if ( !(nUnwantedFound.contains(comp)) ) nUnwantedFound.add(comp);

                    nUnwantedCount++;
                    System.out.println(nUnwantedCount + " - unwanted comparison: " + comp +
                            "\nretrieved with method: " + method +
                            "\nscores: " + nResults.get(res) + "\n");
                }
            }
        }

        ArrayList<String> nWantedMissed = new ArrayList<>(nCompsWanted);
        nWantedMissed.removeAll(nWantedFound);

        ArrayList<String> nUnwantedMissed = new ArrayList<>(nCompsUnwanted);
        nUnwantedMissed.removeAll(nUnwantedFound);

        System.out.println("__________________________________________________");
        System.out.println("Total wanted comparisons: " + nCompsWanted.size() +
                "\nComparisons retrieved: " + nWantedCount +
                "\nComparisons missed: " + (nCompsWanted.size() - nWantedCount) +
                "\n" + nWantedMissed );
        System.out.println("__________________________________________________");
        System.out.println("Total unwanted comparisons: " + nCompsUnwanted.size() +
                "\nComparisons retrieved: " + nUnwantedCount +
                "\nComparisons missed: " + (nCompsUnwanted.size() - nUnwantedCount) +
                "\n" + nUnwantedMissed );
        System.out.println("__________________________________________________");
        //int oWantedFound = 0;
    }
}
