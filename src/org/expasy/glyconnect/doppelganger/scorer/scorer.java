package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
public class scorer {
    public static void main(String[] args) throws Exception {
        String glycanType = "N-Linked";

        ArrayList<doppelganger> gangers = reader.readfiles("proteinsAll", glycanType);


        toTable(glycanType,gangers);
/*
        for (doppelganger doppel : gangers) {
            doppel.attributesChecker();

        }
  */
    }

    public static void toTable(String glycanType, ArrayList<doppelganger> networks) throws FileNotFoundException {
        double cosSimThreshold = 0.85;
        double densityDifferenceMax = 1.0;
        int minNetworkSize = 5;

        String fileName = glycanType+"_minSize"+minNetworkSize+"_CosSim"+cosSimThreshold+"_Density"+densityDifferenceMax;
        PrintStream output = new PrintStream(new File("results/"+fileName+"_TEST_"+".tsv"));
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Network A" + "\t" + "Network B" + "\t"+
                //"Same Cluster"+"\t" +

                "Link Cosine Similarity (virtual T)" + "\t" + "Link Cosine Similarity (virtual F)" + "\t" +

                "Profile Cosine Similarity (virtual T)" + "\t" + "Profile Cosine Similarity (virtual F)" + "\t" +

                "Nodes Number A (Virtual F)" + "\t" + "Nodes Number B (Virtual F)" + "\t" +
                "Links Number A (Virtual F)" + "\t" + "Links Number B (Virtual F)" + "\t" +

                "Density A (Virtual T)" + "\t" + "Density B (Virtual T)" + "\t" +
                "Density Difference (Virtual T)" + "\t" +
                "Density A (Virtual F)" + "\t" + "Density B (Virtual F)" + "\t" +
                "Density Difference (Virtual F)" + "\t" +

                "Virtual Nodes Number A" + "\t" + "Virtual Nodes Number B" + "\t" +
                "Virtual Links Number A" + "\t" + "Virtual Links Number B" + "\t" +

                //"Virtual Nodes Overlap"+"\t"+"Virtual Nodes Jaccard Index"+"\t"+"Virtual Links Overlap"+"\t"+"Virtual Links Jaccard Index"+"\t"+

                "Composition A (virtual T)" + "\t" + "Composition B (virtual T)" + "\t" +
                "Composition A (virtual F)" + "\t" + "Composition B (virtual F)" + "\t" +

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

                    // Networks with less than 6 nodes are not considered
                    if (network1.realNodesNumber() > minNetworkSize && network2.realNodesNumber() > minNetworkSize) {
                        double densityDiffVT = compare.densityDifference(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());
                        double densityDiffVF = compare.densityDifference(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());

                        if (densityDiffVT < densityDifferenceMax && densityDiffVF < densityDifferenceMax) {
                            double linkCosSimVT = compare.cosineSimilarity( helper.frequenciesAsDouble(network1.getLinkFreqVT()),
                                    helper.frequenciesAsDouble(network2.getLinkFreqVT()) );

                            double linkCosSimVF = compare.cosineSimilarity(helper.frequenciesAsDouble(network1.getLinkFreqVF()),
                                    helper.frequenciesAsDouble(network2.getLinkFreqVF()));

                            double propCosSimVT = compare.cosineSimilarity( helper.frequenciesAsDouble( helper.freqsStringToChar(network1.getPropertiesFreqVT()) ),
                                    helper.frequenciesAsDouble( helper.freqsStringToChar(network2.getPropertiesFreqVF()) ) );
                            double propCosSimVF = compare.cosineSimilarity( helper.frequenciesAsDouble( helper.freqsStringToChar(network1.getPropertiesFreqVF()) ),
                                    helper.frequenciesAsDouble( helper.freqsStringToChar(network2.getPropertiesFreqVF()) ) );

                            if (linkCosSimVT >= cosSimThreshold || linkCosSimVF >= cosSimThreshold
                                    || propCosSimVT >= cosSimThreshold || propCosSimVF >= cosSimThreshold ) {

                                String body = network1.getIdentifier() + "\t" + network2.getIdentifier() + "\t" +
                                        linkCosSimVT + "\t" + linkCosSimVF + "\t" +
                                        propCosSimVT + "\t" + propCosSimVF + "\t" +

                                        network1.realNodesNumber() + "\t" + network2.realNodesNumber() + "\t" +
                                        network1.linksNumberVF() + "\t" + network2.linksNumberVF() +"\t" +

                                        network1.getNetworkDensityVT() + "\t" + network2.getNetworkDensityVT() + "\t" +
                                        densityDiffVT + "\t" +
                                        network1.getNetworkDensityVF() + "\t" + network2.getNetworkDensityVF() + "\t" +
                                        densityDiffVF + "\t" +

                                        network1.virtualNodesNumber() + "\t" + network2.virtualNodesNumber() + "\t" +
                                        network1.virtualLinksNumber() + "\t" + network2.virtualLinksNumber() + "\t" +

                                        network1.getLinkStringVT() + "\t" + network2.getLinkStringVT() + "\t" +
                                        network1.getLinkStringVF() + "\t" + network2.getLinkStringVF() + "\t" +

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
}
