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

    public static HashMap<String,String> importResultsTable(String dataset, String type, String method, String score) throws Exception {
        //{identifierA~identifierB, desiredScore}
        HashMap<String,String> results = new HashMap<>();

        String compareTo = dataset+"_"+type+"_minSize5_"+method+".tsv";

        File compareFile = new File("/home/federico/Documenti/Thesis/Doppelganger/results/evaluation/"+compareTo);

        FileReader importFR = new FileReader(compareFile);
        BufferedReader importBR = new BufferedReader(importFR);

        String line = importBR.readLine();
        String[] header = line.split("\\t"); // Using header to retrieve desired method's index
        int scoreIndex = -1;

        for (int i = 0; i < header.length; i++) {
            if ( header[i].equals(score) ) scoreIndex = i;
        }

        if ( scoreIndex == -1 ) {
            System.out.println("QUITTING: Unable to find column '" + score + "' in dataset " + dataset);
        }

        while ( (line = importBR.readLine()) != null ) {
            String[] comparison = line.split("\\t");
            String comp = comparison[0]+"~"+comparison[1];
            String scoreValue = comparison[scoreIndex];

            results.computeIfAbsent(comp, k -> scoreValue);
        }

        return results;
    }
}
