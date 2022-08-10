package org.expasy.glyconnect.doppelganger.QA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class QAImport {
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
        //{identifierA~identifierB, desiredScore}
        HashMap<String,String> results = new HashMap<>();

        String compareTo = dataset+"_"+type+"_minSize5_"+method+"0.0_TEST_"+".tsv";

        File compareFile = new File("/home/federico/Documenti/Thesis/Doppelganger/results/steps/"+compareTo);

        FileReader importFR = new FileReader(compareFile);
        BufferedReader importBR = new BufferedReader(importFR);

        String line = importBR.readLine();
        // TODO: 10/08/22 use first raw to determine the needed column index.
        /* By adding a parameter to the function, it would be possible to select the score one wants to analyse.
         * e.g. real or virtual nodes or link JI, VF or VT cosine similarity.
         * Doing so will enable to apply a threshold during the QA and to compare the differente methods.
         */
        while ( (line = importBR.readLine()) != null ) {
            String[] comparison = line.split("\\t");
            String comp = comparison[0]+"~"+comparison[1];
            String nodesLinksVals = comparison[9]+"~"+comparison[11];
            results.computeIfAbsent(comp, k -> nodesLinksVals);
        }

        return results;
    }
}
