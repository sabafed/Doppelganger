package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
public class scorer {
    public static void main(String[] args) throws Exception {
        ArrayList<doppelganger> gangers = reader.readfiles("proteinsAll");

        String glycanType = "N-Linked";
        toTable(glycanType,gangers);
/*
        for (int f = 0; f < gangers.size(); f++) {
            for (int s = 0; s < gangers.size(); s++) {
                if ( !gangers.get(f).getIdentifier().equals(gangers.get(s).getIdentifier())) {
                    linkSim linkSim1 = new linkSim(gangers.get(f));
                    linkSim linkSim2 = new linkSim(gangers.get(s));

                    if (linkSim1.getLinkStringVT().length() > 0 && linkSim2.getLinkStringVT().length() > 0) {
                        if (compare.densityDifference(linkSim1.getNetworkDensityVT(), linkSim2.getNetworkDensityVT()) < 1.0001 &&
                                compare.densityDifference(linkSim1.getNetworkDensityVF(), linkSim2.getNetworkDensityVF()) < 1.0001) {
                            System.out.println("\n" + linkSim1.getIdentifier() + "                           |   " + linkSim2.getIdentifier() +
                                    "\nNodesVT:      " + linkSim1.nodesNumberVT() + "                        |   " + linkSim2.nodesNumberVT() +
                                    "\nLinksVT:      " + linkSim1.linksNumberVT() + "                        |   " + linkSim2.linksNumberVT() +
                                    "\nNetDensityVT: " + linkSim1.getNetworkDensityVT() + "                  |   " + linkSim2.getNetworkDensityVT() +
                                    "\nNodesVF:      " + linkSim1.realNodesNumber() + "                      |   " + linkSim2.realNodesNumber() +
                                    "\nLinksVF:      " + linkSim1.linksNumberVF() + "                        |   " + linkSim2.linksNumberVF() +
                                    "\nNetDensityVF: " + linkSim1.getNetworkDensityVF() + "                  |   " + linkSim2.getNetworkDensityVF() +
                                    "\nNetDensityDifferenceVT: " + compare.densityDifference(linkSim1.getNetworkDensityVT(), linkSim2.getNetworkDensityVT()) +
                                    "\nNetDensityDifferenceVF: " + compare.densityDifference(linkSim1.getNetworkDensityVF(), linkSim2.getNetworkDensityVF()) +
                                    "\n\nLinkMapVT:            " + linkSim1.getLinkCountVT() +
                                    "\n                      " + linkSim2.getLinkCountVT() +
                                    "\nLinkMapVF:            " + linkSim1.getLinkCountVF() +
                                    "\n                      " + linkSim2.getLinkCountVF() +
                                    "\nCosineSimilarityVT: " + compare.cosineSimilarity(linkSim1.plainFrequencies(linkSim1.getLinkFreqVT()),
                                                                                                linkSim2.plainFrequencies(linkSim2.getLinkFreqVT())) +
                                    "\nCosineSimilarityVF: " + compare.cosineSimilarity(linkSim1.plainFrequencies(linkSim1.getLinkFreqVF()),
                                                                                            linkSim2.plainFrequencies(linkSim2.getLinkFreqVF())) +

                                    "\n_____________________________________________________________________________________________________________________________");
                        }
                    }
                }
            }
        }*/
    }

    public static void toTable(String glycanType, ArrayList<doppelganger> networks) throws FileNotFoundException {
        double cosSimThreshold = 0.85;
        double densityDifferenceMax = 1.0;

        String fileName = "_CosSim"+cosSimThreshold+"_Density"+densityDifferenceMax;
        PrintStream output = new PrintStream(new File("results/"+glycanType+fileName+"_TEST_"+".tsv"));
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Network A" + "\t" + "Network B" + "\t"/*+"Same Cluster"+"\t"*/ +
                "Link Cosine Similarity (virtual T)" + "\t" + "Link Cosine Similarity (virtual F)" + "\t" +
                //"Profile Cosine Similarity (virtual T)"+"\t"+"Profile Cosine Similarity (virtual F)"+"\t"+
                "Nodes Number A (Virtual F)" + "\t" + "Nodes Number B (Virtual F)" + "\t" +
                "Links Number A (Virtual F)" + "\t" + "Links Number B (Virtual F)" + "\t" +

                "Density A (Virtual T)" + "\t" + "Density B (Virtual T)" + "\t" +
                "Density Difference (Virtual T)" + "\t" +
                "Density A (Virtual F)" + "\t" + "Density B (Virtual F)" + "\t" +
                "Density Difference (Virtual F)" + "\t" +

                "Virtual Nodes Number A" + "\t" + "Virtual Nodes Number B" + "\t" +
                "Virtual Links Number A" + "\t" + "Virtual Links Number B" + "\t" +

                //"Virtual Nodes Overlap"+"\t"+"Virtual Nodes Jaccard Index"+"\t"+"Virtual Links Overlap"+"\t"+"Virtual Links Jaccard Index"+"\t"+

                //"Composition A (virtual T)"+"\t"+ "Composition B (virtual T)"+"\t"+"Composition A (virtual F)"+"\t"+"Composition B (virtual F)"+"\t"+
                "Link counts A (virtual T)" + "\t" + "Link counts B (virtual T)" + "\t" +
                "Frequencies A (virtual T)" + "\t" + "Frequencies B (virtual T)" + "\t" +

                "Link counts A (virtual F)" + "\t" + "Link counts B (virtual F)" + "\t" +
                "Frequencies A (virtual F)" + "\t" + "Frequencies B (virtual F)";

        System.out.println(header);

        for (int f = 0; f < networks.size(); f++) {
            for (int s = 0; s < networks.size(); s++) {
                if ( !networks.get(f).getIdentifier().equals(networks.get(s).getIdentifier()) ) {
                    linkSim linkSim1 = new linkSim(networks.get(f));
                    linkSim linkSim2 = new linkSim(networks.get(s));

                    // If the linkString that includes virtual and real link is 0 it means that the network is empty
                    if (linkSim1.getLinkStringVT().length() > 0 && linkSim2.getLinkStringVT().length() > 0) {
                        double densityDiffVT = compare.densityDifference(linkSim1.getNetworkDensityVT(), linkSim2.getNetworkDensityVT());
                        double densityDiffVF = compare.densityDifference(linkSim1.getNetworkDensityVF(), linkSim2.getNetworkDensityVF());

                        if (densityDiffVT < densityDifferenceMax && densityDiffVF < densityDifferenceMax) {
                            double cosSimVT = compare.cosineSimilarity( linkSim1.plainFrequencies(linkSim1.getLinkFreqVT()),
                                    linkSim2.plainFrequencies(linkSim2.getLinkFreqVT()) );

                            double cosSimVF = compare.cosineSimilarity(linkSim1.plainFrequencies(linkSim1.getLinkFreqVF()),
                                    linkSim2.plainFrequencies(linkSim2.getLinkFreqVF()));

                            if (cosSimVT >= cosSimThreshold || cosSimVF >= cosSimThreshold) {
                                String body = linkSim1.getIdentifier() + "\t" + linkSim2.getIdentifier() + "\t" +
                                        cosSimVT + "\t" + cosSimVF + "\t" +
                                        linkSim1.realNodesNumber() + "\t" + linkSim2.realNodesNumber() + "\t" +
                                        linkSim1.linksNumberVF() + "\t" + linkSim2.linksNumberVF() +"\t" +

                                        linkSim1.getNetworkDensityVT() + "\t" + linkSim2.getNetworkDensityVT() + "\t" +
                                        densityDiffVT + "\t" +
                                        linkSim1.getNetworkDensityVF() + "\t" + linkSim2.getNetworkDensityVF() + "\t" +
                                        densityDiffVF + "\t" +

                                        linkSim1.virtualNodesNumber() + "\t" + linkSim2.virtualNodesNumber() + "\t" +
                                        linkSim1.virtualLinksNumber() + "\t" + linkSim2.virtualLinksNumber() + "\t" +

                                        linkSim1.getLinkCountVT() + "\t" + linkSim2.getLinkCountVT() + "\t" +
                                        linkSim1.getLinkFreqVT() + "\t" + linkSim2.getLinkFreqVT() + "\t" +

                                        linkSim1.getLinkCountVF() + "\t" + linkSim2.getLinkCountVF() + "\t" +
                                        linkSim1.getLinkFreqVF() + "\t" + linkSim2.getLinkFreqVF() + "\t";

                                System.out.println(body);
                            }
                        }
                    }
                }
            }
        }

        System.setOut(console);
        System.out.println("File '"+fileName+"' has been created!");
    }
}
