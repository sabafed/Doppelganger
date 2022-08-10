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

        /*
        ArrayList<doppelganger> doppelgangers = reader.readfiles(proteinsAll,oLinked);
        QAExport.jaccardIndexToTable(oLinked,doppelgangers,proteinsAll,"JaccardIndex");
        */
        statistics(proteinsAll, nLinked, "JaccardIndex");
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

    public static void statistics(String dataset, String glycanType, String method) throws Exception {

        HashMap<String,String> results = QAImport.importResultsTable(dataset, glycanType, method);

        ArrayList<String> positives = QAImport.importQAComparisons(glycanType, dataset, "Wanted");
        ArrayList<String> negatives = QAImport.importQAComparisons(glycanType, dataset, "Unwanted");

        ArrayList<String> truePositives =  new ArrayList<>(); // Positive comparisons found in the results
        ArrayList<String> falsePositives = new ArrayList<>(); // Negative comparisons found in the results

        for (String res : results.keySet()) {
            for (String comp : positives) {
                if ( comp.equals(res) ) {
                    if ( !(truePositives.contains(comp)) ) truePositives.add(comp);

                    System.out.println(truePositives.size() +" - wanted comparison: " + comp +
                            "\nretrieved with method: " + method +
                            "\nscores: " + results.get(res) + "\n");
                }
            }
        }

        for (String res : results.keySet()) {
            for (String comp : negatives) {
                if (res.equals(comp)) {
                    if ( !(falsePositives.contains(comp)) ) falsePositives.add(comp);

                    System.out.println(falsePositives.size() + " - unwanted comparison: " + comp +
                            "\nretrieved with method: " + method +
                            "\nscores: " + results.get(res) + "\n");
                }
            }
        }

        ArrayList<String> falseNegatives = new ArrayList<>(positives); // Positive comparisons NOT found in the results
        falseNegatives.removeAll(truePositives);

        ArrayList<String> trueNegatives = new ArrayList<>(negatives); // Negative comparisons NOT found in the results
        trueNegatives.removeAll(falsePositives);

        System.out.println("_______________________"+glycanType+"___________________________");
        System.out.println(
                  "Total Positive Comparisons: " + positives.size() +
                "\nTrue  Positive Comparisons: " + truePositives.size() +
                "\nFalse Positive Comparisons: " + falsePositives.size() +
                "\n" + falsePositives);
        System.out.println("__________________________________________________");
        System.out.println(
                  "Total Negative Comparisons: " + negatives.size() +
                "\nTrue  Negative Comparisons: " + trueNegatives.size() +
                "\nFalse Negative Comparisons: " + falseNegatives +
                "\n" + falseNegatives);
        System.out.println("__________________________________________________________");
        //int oWantedFound = 0;

        int TP = truePositives.size();
        int TN = trueNegatives.size();
        int FP = falsePositives.size();
        int FN = falseNegatives.size();

        double MCC = statistics.matthewsCorrelationCoefficient(TP,TN,FP,FN);

        System.out.println("MCC score is: " + MCC);
    }
}
