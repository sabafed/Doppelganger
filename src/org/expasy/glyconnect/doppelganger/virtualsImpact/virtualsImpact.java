package org.expasy.glyconnect.doppelganger.virtualsImpact;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class virtualsImpact {
    public static void main(String[] args) throws Exception {

        String sourceDirectory = "proteinsAll";
        //String sourceDirectory = "sourcesAll/tissue";

        //String glycanType = "N-Linked";
        String glycanType = "O-Linked";

        ArrayList<doppelganger> gangers = reader.readfiles(sourceDirectory, glycanType);

        doppelgangersToTable(sourceDirectory, glycanType, gangers);
    }

    public static void doppelgangersToTable(String sourceDirectory, String glycanType, ArrayList<doppelganger> gangers) throws FileNotFoundException {
        sourceDirectory = sourceDirectory.replace("/", "_");
        String fileName = sourceDirectory+"_"+glycanType+"_VirtualsImpact_Summary";
        String targetDirectory = "results/virtualsImpact/";
        PrintStream output = new PrintStream(new File(targetDirectory+fileName+"_TEST_"+".tsv"));
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Identifier" + "\t"+ "Taxonomy" + "\t" +

                "Nodes Number (Virtual F)" + "\t" + "Nodes Number (Virtual T)" + "\t" +
                "Links Number (Virtual F)" + "\t" + "Links Number (Virtual T)" + "\t" +

                "Density (Virtual F)" + "\t" + "Density (Virtual T)" + "\t" +

                "Virtual Nodes Number" + "\t" + "Virtual Links Number" + "\t" +

                "Links String (virtual F)" + "\t" + "Links String (virtual T)" + "\t" +
                "Link Transitions (virtual F)" + "\t" + "Link Transitions (virtual T)" + "\t" +

                "Link counts (virtual F)" + "\t" + "Link counts (virtual T)" + "\t" +
                "Frequencies (virtual F)" + "\t" + "Frequencies (virtual T)" + "\t" +

                "Node properties (virtual F)" + "\t" +  "Node properties (virtual T)"
                ;

        System.out.println(header);

        int networkNumber = 0;

        int meanNodesNumVF = 0;
        int meanNodesNumVT = 0;

        int meanLinksNumVF = 0;
        int meanLinksNumVT = 0;

        double meanDensityVF = 0.0;
        double meanDensityVT = 0.0;

        int meanVirtualNodesNum = 0;
        int meanVirtualLinksNum = 0;

        TreeMap<String, Integer> allTransitionsVF = new TreeMap<>();
        TreeMap<String, Integer> allTransitionsVT = new TreeMap<>();

        HashMap<Character, Integer> allLinkCountVF = new HashMap<>();
        HashMap<Character, Integer> allLinkCountVT = new HashMap<>();

        for (doppelganger ganger : gangers) {
            if ( ganger.getIdentifier() != null ) {
                // With OR condition it is possible to see how the presence/absence of virtual nodes impacts the graph.
                if ( ganger.getLinkTransitionsVF().size() > 0 || ganger.getLinkTransitionsVT().size() > 0 ) {
                    networkNumber++;
                    String id = ganger.getIdentifier();
                    String taxonomy = ganger.getGETObject().getTaxonomies().get(0).toString();

                    int nodesNumVF = (ganger.nodesNumberVF());
                    meanNodesNumVF += nodesNumVF;

                    int nodesNumVT = (ganger.nodesNumberVT());
                    meanNodesNumVT += nodesNumVT;

                    int linksNumVF = (ganger.linksNumberVF());
                    meanLinksNumVF += linksNumVF;

                    int linksNumVT = (ganger.linksNumberVT());
                    meanLinksNumVT += linksNumVT;

                    double densityVF = (ganger.getNetworkDensityVF());
                    meanDensityVF += densityVF;

                    double densityVT = (ganger.getNetworkDensityVT());
                    meanDensityVT += densityVT;

                    int virtualNodesNum = (ganger.virtualNodesNumber());
                    meanVirtualNodesNum += virtualNodesNum;

                    int virtualLinksNum = (ganger.virtualLinksNumber());
                    meanVirtualLinksNum += virtualLinksNum;

                    String linkStringVF = ganger.getLinkStringVF();
                    String linkStringVT = ganger.getLinkStringVT();

                    TreeMap<String, Integer> linkTransitionsVF = ganger.getLinkTransitionsVF();
                    for ( String k : linkTransitionsVF.keySet() ) {
                        if (!allTransitionsVF.containsKey(k)) allTransitionsVF.put(k, linkTransitionsVF.get(k));
                        else allTransitionsVF.replace(k, (allTransitionsVF.get(k)+linkTransitionsVF.get(k)) );
                    }

                    TreeMap<String, Integer> linkTransitionsVT = ganger.getLinkTransitionsVT();
                    for ( String k : linkTransitionsVT.keySet() ) {
                        if ( !allTransitionsVT.containsKey(k) ) allTransitionsVT.put(k, linkTransitionsVT.get(k));
                        else allTransitionsVT.replace(k, (allTransitionsVT.get(k)+linkTransitionsVT.get(k)) );
                    }

                    HashMap<Character, Integer> linkCountVF = ganger.getLinkCountVF();
                    for ( Character k : linkCountVF.keySet() ) {
                        if ( !allLinkCountVF.containsKey(k) ) allLinkCountVF.put(k, linkCountVF.get(k));
                        else allLinkCountVF.replace(k, (allLinkCountVF.get(k)+linkCountVF.get(k)) );
                    }

                    HashMap<Character, Integer> linkCountVT = ganger.getLinkCountVT();
                    for ( Character k : linkCountVT.keySet() ) {
                        if ( !allLinkCountVT.containsKey(k) ) allLinkCountVT.put(k, linkCountVT.get(k));
                        else allLinkCountVT.replace(k, (allLinkCountVT.get(k)+linkCountVT.get(k)) );
                    }

                    HashMap<Character, Double> linkFreqVF = ganger.getLinkFreqVF();
                    HashMap<Character, Double> linkFreqVT = ganger.getLinkFreqVT();

                    HashMap<String, Double> propertiesFreqVF = ganger.getPropertiesFreqVF();
                    HashMap<String, Double> propertiesFreqVT = ganger.getPropertiesFreqVT();

                    String body = id + "\t" + taxonomy + "\t" +

                            nodesNumVF + "\t" + nodesNumVT + "\t" +
                            linksNumVF + "\t" + linksNumVT + "\t" +

                            densityVF + "\t" + densityVT + "\t" +

                            virtualNodesNum + "\t" + virtualLinksNum + "\t" +

                            linkStringVF + "\t" + linkStringVT + "\t" +
                            linkTransitionsVF + "\t" + linkTransitionsVT + "\t" +

                            linkCountVF + "\t" + linkCountVT + "\t" +
                            linkFreqVF + "\t" + linkFreqVT + "\t" +

                            propertiesFreqVF + "\t" + propertiesFreqVT;

                    System.out.println(body);
                }
            }
        }
        String summaryId = "Summary";
        String summaryTaxa = "All taxa";

        meanNodesNumVF = meanNodesNumVF/networkNumber;
        meanNodesNumVT = meanNodesNumVT/networkNumber;

        meanLinksNumVF = meanLinksNumVF/networkNumber;
        meanLinksNumVT = meanLinksNumVT/networkNumber;

        meanDensityVF = meanDensityVF/(double) networkNumber;
        meanDensityVT = meanDensityVT/(double) networkNumber;

        meanVirtualNodesNum = meanVirtualNodesNum/networkNumber;
        meanVirtualLinksNum = meanVirtualLinksNum/networkNumber;

        String linkStrings = "Na"; // Remember to print twice.

        //HashMap<Character, Double> allLinkFreqVF = helper.linkFrequencies(allLinkCountVF,)
        HashMap<Character, Double> allLinkFreqVT = new HashMap<>();

        HashMap<String, Double> allPropertiesFreqVF = new HashMap<>();
        HashMap<String, Double> allPropertiesFreqVT = new HashMap<>();

        String summary = summaryId + "\t" + summaryTaxa + "\t" +
                meanNodesNumVF + "\t" + meanNodesNumVT + "\t" +
                meanLinksNumVF + "\t" + meanLinksNumVT + "\t" +
                meanDensityVF + "\t" + meanDensityVT + "\t" +
                meanVirtualNodesNum + "\t" + meanVirtualLinksNum + "\t" +
                /*linkStrings +/* "\t" + /*linkStrings +*/ "\t" +
                allTransitionsVF + "\t" + allTransitionsVT + "\t" //+
                //linkStrings + "\t" + linkStrings + "\t" + linkStrings + "\t" + linkStrings + "\t"
                ;
        System.out.println(summary);

        System.setOut(console);
        System.out.println("File '"+fileName+"' has been created in directory '"+targetDirectory+"'");
        //for ( String k : allTransitionsVT.keySet() ) System.out.print(allTransitionsVT.get(k)+",");
    }
}
