package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.util.ArrayList;
public class scorer {
    public static void main(String[] args) throws Exception {
        ArrayList<doppelganger> gangers = reader.readfiles("proteinsAll");

        String glycanType = "N-Linked";
        //toTable(glycanType,gangers);

        for (doppelganger doppel : gangers) {

            System.out.println(doppel.getIdentifier());

            System.out.println("Real nodes: "+ doppel.getRealNodes() +
                    "\nVirtual nodes: "+ doppel.getVirtualNodes() +
                    "\nReal nodes number: "+ doppel.realNodesNumber() +
                    "\nVirtual nodes number: "+doppel.virtualNodesNumber() +
                    "\nReal links: "+doppel.getLinkStringVT() +
                    "\nVirtual links: "+doppel.getLinkStringVF() +
                    "\nNetwork density VT: "+doppel.getNetworkDensityVT() +
                    "\nNetwork density VF: "+doppel.getNetworkDensityVF()+
                    "\n\nProperties count VT: "+doppel.getPropertiesCountVT()+
                    "\nProperties count VF: "+doppel.getPropertiesCountVF()+
                    "\nProperties frequencies VT: "+doppel.getPropertiesFreqVT() +
                    "\nProperties frequencies VF: "+doppel.getPropertiesFreqVF() +
                    "\n\nLink count VT: "+doppel.getLinkCountVT() +
                    "\nLink count VF: "+doppel.getLinkCountVF() +
                    "\nLink frequencies VT: "+doppel.getLinkFreqVT() +
                    "\nLink frequencies VF: "+doppel.getLinkFreqVF() +
                    "\n_________________________________________________________________________________________________________________________________");
        }
    }
/*
    public static void toTable(String glycanType, ArrayList<doppelganger> networks) throws FileNotFoundException {
        double cosSimThreshold = 0.85;
        double densityDifferenceMax = 1.0;

        String fileName = "_CosSim"+cosSimThreshold+"_Density"+densityDifferenceMax;
        PrintStream output = new PrintStream(new File("results/"+glycanType+fileName+"_TEST_"+".tsv"));
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Network A" + "\t" + "Network B" + "\t"+
                //"Same Cluster"+"\t" +

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
                    doppelganger network1 = networks.get(f);
                    doppelganger network2 = networks.get(s);

                    // If the linkString that includes virtual and real link is 0 it means that the network is empty
                    if (network1.getLinkStringVT().length() > 0 && network2.getLinkStringVT().length() > 0) {
                        double densityDiffVT = compare.densityDifference(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());
                        double densityDiffVF = compare.densityDifference(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());

                        if (densityDiffVT < densityDifferenceMax && densityDiffVF < densityDifferenceMax) {
                            double linkCosSimVT = compare.cosineSimilarity( linkSim.frequenciesAsDouble(network1.getLinkFreqVT()),
                                    linkSim.frequenciesAsDouble(network2.getLinkFreqVT()) );

                            double linkCosSimVF = compare.cosineSimilarity(linkSim.frequenciesAsDouble(network1.getLinkFreqVF()),
                                    linkSim.frequenciesAsDouble(network2.getLinkFreqVF()));

                            if (linkCosSimVT >= cosSimThreshold || linkCosSimVF >= cosSimThreshold) {
                                String body = network1.getIdentifier() + "\t" + network2.getIdentifier() + "\t" +
                                        linkCosSimVT + "\t" + linkCosSimVF + "\t" +
                                        network1.realNodesNumber() + "\t" + network2.realNodesNumber() + "\t" +
                                        network1.linksNumberVF() + "\t" + network2.linksNumberVF() +"\t" +

                                        network1.getNetworkDensityVT() + "\t" + network2.getNetworkDensityVT() + "\t" +
                                        densityDiffVT + "\t" +
                                        network1.getNetworkDensityVF() + "\t" + network2.getNetworkDensityVF() + "\t" +
                                        densityDiffVF + "\t" +

                                        network1.virtualNodesNumber() + "\t" + network2.virtualNodesNumber() + "\t" +
                                        network1.virtualLinksNumber() + "\t" + network2.virtualLinksNumber() + "\t" +

                                        network1.getLinkCountVT() + "\t" + network2.getLinkCountVT() + "\t" +
                                        network1.getLinkFreqVT() + "\t" + network2.getLinkFreqVT() + "\t" +

                                        network1.getLinkCountVF() + "\t" + network2.getLinkCountVF() + "\t" +
                                        network1.getLinkFreqVF() + "\t" + network2.getLinkFreqVF() + "\t";

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
    */
}
