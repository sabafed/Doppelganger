package org.expasy.glyconnect.doppelganger.doppelganger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class.
 *
 * Its purpose is to obtain a doppelganger object for each file in a directory.
 */
public class reader {
    public static void main(String[] args) throws Exception {
        String glycanType = "N-Linked";

        ArrayList<doppelganger> gangers = new ArrayList<>(readfiles("proteinsAll", glycanType));

        // Testing link composition comparison
        for (int i = 0; i < gangers.size(); i++) {
            if (i < gangers.size() - 1){
                doppelganger net1 = gangers.get(i);
                doppelganger net2 = gangers.get(i + 1);

                if ( net1.getLinkStringCompositionVT().length() != 0 && net2.getLinkStringCompositionVT().length() != 0) {
                    System.out.println("\nIdentifiers: " + net1.getIdentifier() + " ~ " + net2.getIdentifier());
                    System.out.println("VT: " + net1.getLinkStringCompositionVT() + " ~ " + net2.getLinkStringCompositionVT());
                    System.out.println("VF: " + net1.getLinkStringCompositionVF() + " ~ " + net2.getLinkStringCompositionVF());
                }
            }
        }


        /* Testing sequence cluster analysis
        ArrayList<String> proteins = new ArrayList<>();

        for (String k : clusters.keySet()) {
            proteins.addAll(clusters.get(k));
        }

        for (int i = 0; i < proteins.size(); i++) {
            for (int j = 0; j < proteins.size(); j++) {
                String prot1 = proteins.get(i);
                String prot2 = proteins.get(j);


                for (String k : clusters.keySet()) {
                    if ( k.equals(prot1) || k.equals(prot2) ) {
                        if ( clusters.get(k).contains(prot1) && clusters.get(k).contains(prot2) )
                            System.out.println("prot1: "+prot1+"\nprot2: "+prot2+"\nmap key: "+k+"\ncluster: "+clusters.get(k)+"\n");
                    }
                }
            }
        }
*/
    }

    public static ArrayList<doppelganger> readfiles(String sourceDirectory, String glycanType) throws Exception {
        ArrayList<doppelganger> gangers = new ArrayList<>();
        File directory = new File("/home/federico/Documenti/Thesis/Doppelganger/dataAll/"+sourceDirectory+"/"+glycanType);
        File[] files = directory.listFiles();

        //int totalDoiless = 0;

        for (File file : files) {
            Path doiJson = Path.of(String.valueOf(file));
            doppelganger doppel = new doppelganger(doiJson);

            if ( !doppel.isInside(gangers) ) gangers.add(doppel);

            //totalDoiless += doppel.doiless;
            //System.out.println(doppel.getIdentifier()+"\n___________________________________________________________________");
        }

        return gangers;
    }

    public static HashMap<String,ArrayList<String>> importClusters() throws Exception {
        HashMap<String,ArrayList<String>> clusterMap = new HashMap<>();

        File clustersFile = new File("/home/federico/Documenti/Thesis/Doppelganger/dataAll/clusters/proteins-all_cluster.tsv");

        FileReader clustersFR = new FileReader(clustersFile);
        BufferedReader clustersBR = new BufferedReader(clustersFR);

        String line;

        while ( (line = clustersBR.readLine()) != null ) {
            String[] cluster = line.split("\\t");
            clusterMap.computeIfAbsent(cluster[0], k -> new ArrayList<String>());
            if ( !clusterMap.get(cluster[0]).contains(cluster[1]) ) clusterMap.get(cluster[0]).add(cluster[1]);
        }

        return clusterMap;
    }

    public static String clusterRepresentative(ArrayList<doppelganger> gangers) throws Exception {
        String representative = "";

        HashMap<String, ArrayList<String>> clusters = importClusters();

        for (int i = 0; i < gangers.size(); i++) {
            for (int j = 0; j < gangers.size(); j++) {
                String id1 = gangers.get(i).getIdentifier().split(";")[1];
                String id2 = gangers.get(j).getIdentifier().split(";")[1];

                for (String k : clusters.keySet()) {
                    if ( k.equals(id1) || k.equals(id2) ) {
                        if ( clusters.get(k).contains(id1) && clusters.get(k).contains(id2) ) {
                            return representative;
                            //System.out.println(k);
                            //System.out.println("prot1: "+id1+"\nprot2: "+id2+"\nmap key: "+k+"\ncluster: "+clusters.get(k)+"\n");
                        }
                    }
                }
            }
        }
        return representative;
    }
}
