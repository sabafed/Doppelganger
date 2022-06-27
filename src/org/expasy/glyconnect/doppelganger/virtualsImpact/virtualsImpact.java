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
        String glycanType = "N-Linked";
        ArrayList<doppelganger> gangers = reader.readfiles(sourceDirectory, glycanType);

        doppelgangersToTable(sourceDirectory, glycanType, gangers);
    }

    public static void doppelgangersToTable(String sourceDirectory, String glycanType, ArrayList<doppelganger> gangers) throws FileNotFoundException {
        sourceDirectory = sourceDirectory.replace("/", "_");
        String fileName = sourceDirectory+"_"+glycanType+"_VirtualsImpact";
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

        for (doppelganger ganger : gangers) {
            if ( ganger.getIdentifier() != null ) {
                // With OR condition it is possible to see how the presence/absence of virtual nodes impacts the graph.
                if ( ganger.getLinkTransitionsVF().size() > 0 || ganger.getLinkTransitionsVT().size() > 0 ) {
                    String id = ganger.getIdentifier();
                    String taxonomy = ganger.getGETObject().getTaxonomies().get(0).toString();

                    String nodesNumVF = String.valueOf(ganger.nodesNumberVF());
                    String nodesNumVT = String.valueOf(ganger.nodesNumberVT());

                    String linksNumVF = String.valueOf(ganger.linksNumberVF());
                    String linksNumVT = String.valueOf(ganger.linksNumberVT());

                    String densityVF = String.valueOf(ganger.getNetworkDensityVF());
                    String densityVT = String.valueOf(ganger.getNetworkDensityVT());

                    String virtualNodesNum = String.valueOf(ganger.virtualNodesNumber());
                    String virtualLinksNum = String.valueOf(ganger.virtualLinksNumber());

                    String linkStringVF = ganger.getLinkStringVF();
                    String linkStringVT = ganger.getLinkStringVT();

                    TreeMap<String, Integer> linkTransitionsVF = ganger.getLinkTransitionsVF();
                    TreeMap<String, Integer> linkTransitionsVT = ganger.getLinkTransitionsVT();

                    HashMap<Character, Integer> linkCountVF = ganger.getLinkCountVF();
                    HashMap<Character, Integer> linkCountVT = ganger.getLinkCountVT();

                    HashMap<Character, Double> linkFreqVF = ganger.getLinkFreqVF();
                    HashMap<Character, Double> linkFreqVT = ganger.getLinkFreqVT();

                    HashMap<String, Double> propertiesFreqVF = ganger.getPropertiesFreqVF();
                    HashMap<String, Double> propertiesFreqVT = ganger.getPropertiesFreqVT();

                    String body = id + "\t" + taxonomy + "\t" +

                            nodesNumVF + "\t" + nodesNumVT + "\t" +
                            linksNumVF + "\t" + linksNumVT + "\t" +

                            densityVF + "\t" + densityVT + "\t" +

                            virtualNodesNum + "\t" + virtualLinksNum + "\t" +

                            linkStringVF + "\t" + linksNumVT + "\t" +
                            linkTransitionsVF + "\t" + linkTransitionsVT + "\t" +

                            linkCountVF + "\t" + linkCountVT + "\t" +
                            linkFreqVF + "\t" + linkFreqVT + "\t" +

                            propertiesFreqVF + "\t" + propertiesFreqVT;

                    System.out.println(body);
                }
            }
        }

        System.setOut(console);
        System.out.println("File '"+fileName+"' has been created in directory '"+targetDirectory+"'");
    }
}
