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
        //String glycanType = "O-Linked";

        //String sourceDirectory = "referencesAll";
        String sourceDirectory = "proteinsAll";
        //String sourceDirectory = "diseasesAll"; Also have taxonomy columns
        //String sourceDirectory = "cellLinesAll";

        //String sourceDirectory = "sourcesAll/cell_component"; Also have taxonomy columns
        //String sourceDirectory = "sourcesAll/cell_type"; Also have taxonomy columns
        //String sourceDirectory = "sourcesAll/tissue"; Also have taxonomy columns
        //String sourceDirectory = "sourcesAll/tissue_plant"; Also have taxonomy columns

        ArrayList<doppelganger> gangers = reader.readfiles(sourceDirectory, glycanType);
        //for (doppelganger doppel: gangers) System.out.println(doppel.getIdentifier()+" : "+doppel.nodesNumberVF());
        //for (doppelganger doppel : gangers) doppel.attributesChecker();
        toTable(glycanType, gangers, sourceDirectory);

/* For DEBUG
        int total = 0;
        int intersection = 0;
        double cosSimThreshold = 0.85;

        for (int i = 0; i < gangers.size(); i++) {
            for (int j = 0; j < gangers.size(); j++) {
                doppelganger test1 = gangers.get(i);
                doppelganger test2 = gangers.get(j);

                if ( !test1.equals(test2) ) {
                    if ( test1.getRealNodes().size() > 5 && test2.getRealNodes().size() > 5 ) {
                        double densityDiffVF = compare.densityDifference(test1.getNetworkDensityVF(), test2.getNetworkDensityVF());

                        if ( densityDiffVF < 10 ) {
                            double linkCosSimVT = compare.cosineSimilarity(helper.frequenciesAsDouble(test1.getLinkFreqVT()),
                                    helper.frequenciesAsDouble(test2.getLinkFreqVT()));

                            double linkCosSimVF = compare.cosineSimilarity(helper.frequenciesAsDouble(test1.getLinkFreqVF()),
                                    helper.frequenciesAsDouble(test2.getLinkFreqVF()));

                            double propCosSimVT = compare.cosineSimilarity(helper.frequenciesAsDouble(helper.freqsStringToChar(test1.getPropertiesFreqVT())),
                                    helper.frequenciesAsDouble(helper.freqsStringToChar(test2.getPropertiesFreqVF())));
                            double propCosSimVF = compare.cosineSimilarity(helper.frequenciesAsDouble(helper.freqsStringToChar(test1.getPropertiesFreqVF())),
                                    helper.frequenciesAsDouble(helper.freqsStringToChar(test2.getPropertiesFreqVF())));

                            if ( linkCosSimVT >= cosSimThreshold || linkCosSimVF >= cosSimThreshold
                                    || propCosSimVT >= cosSimThreshold || propCosSimVF >= cosSimThreshold ) {

                                System.out.println(test1.getIdentifier() + "\t" + test2.getIdentifier() + "\t" +
                                        compare.linkIntersection(test1.getRealLinks(), test2.getRealLinks()));
                            }
                        }
                    }
                }
            }
        }
*/

    }

    public static void toTable(String glycanType, ArrayList<doppelganger> networks, String sourceDirectory) throws FileNotFoundException {
        double cosSimThreshold = 0.90;
        double densityDifferenceMax = 1.05;
        int minNetworkSize = 5;

        //sourcesAll folder contains subfolders tha could get in the way of file saving
        sourceDirectory = sourceDirectory.replace("/", "_");
        String targetDirectory = "results/";
        String fileName = sourceDirectory+"_"+glycanType+"_minSize"+minNetworkSize+"_CosSim"+cosSimThreshold+"_Density"+densityDifferenceMax+"_LinkComposition"+"_JaccardIndex";
        PrintStream output = new PrintStream(new File(targetDirectory+fileName+"_TEST_"+".tsv"));
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Network A" + "\t" + "Network B" + "\t"+
                "Taxonomy A" + "\t" + "Taxonomy B" + "\t" +
                //"Same Cluster"+"\t" +

                "Link Cosine Similarity (virtual T)" + "\t" + "Link Cosine Similarity (virtual F)" + "\t" +

                "Profile Cosine Similarity (virtual T)" + "\t" + "Profile Cosine Similarity (virtual F)" + "\t" +

                "Nodes Number A (Virtual F)" + "\t" + "Nodes Number B (Virtual F)" + "\t" +
                "Links Number A (Virtual F)" + "\t" + "Links Number B (Virtual F)" + "\t" +
                "Real Nodes Overlap" + "\t" + "Real Nodes Jaccard Index" + "\t" +
                "Real Links Overlap" + "\t" + "Real Links Jaccard Index" + "\t" +

                "Density A (Virtual T)" + "\t" + "Density B (Virtual T)" + "\t" +
                "Density Difference (Virtual T)" + "\t" + "Density Ratio (Virtual T)" + "\t" +
                "Density A (Virtual F)" + "\t" + "Density B (Virtual F)" + "\t" +
                "Density Difference (Virtual F)" + "\t" + "Density Ratio (Virtual F)" + "\t" +

                "Virtual Nodes Number A" + "\t" + "Virtual Nodes Number B" + "\t" +
                "Virtual Links Number A" + "\t" + "Virtual Links Number B" + "\t" +

                "Virtual Nodes Overlap" + "\t" + "Virtual Nodes Jaccard Index" + "\t" +
                "Virtual Links Overlap" + "\t" + "Virtual Links Jaccard Index" + "\t" +

                "Links String A (virtual T)" + "\t" + "Links String B (virtual T)" + "\t" +
                "Links String A (virtual F)" + "\t" + "Links String B (virtual F)" + "\t" +

                "Link Transitions A (virtual T)" + "\t" + "Link Transitions A (virtual F)" + "\t" +
                "Link Transitions B (virtual T)" + "\t" + "Link Transitions B (virtual F)" + "\t" +

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
                    if ( network1.realNodesNumber() > minNetworkSize && network2.realNodesNumber() > minNetworkSize ) {
                        double densityDiffVT = compare.densityDifference(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());
                        double densityDiffVF = compare.densityDifference(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());

                        double densityRatioVT = compare.densityRatio(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());
                        double densityRatioVF = compare.densityRatio(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());

                        if ( densityDiffVT < densityDifferenceMax && densityDiffVF < densityDifferenceMax ) {
                        //if ( densityRatioVT < 1.5 && densityRatioVT > 0.95 && densityDiffVF < 1.5 && densityDiffVF > 0.95 ) {
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

                                int realNodesOverlap =
                                        compare.nodeInteresectionSize(network1.getRealNodes(),network2.getRealNodes());
                                int realNodesUnion =
                                        compare.nodeUnionSize(network1.getRealNodes(),network2.getRealNodes());
                                double realNodesJaccardIndex = compare.jaccardIndex(realNodesOverlap,realNodesUnion);

                                int realLinksOverlap = compare.linkInteresectionSize(network1.getRealLinks(),network2.getRealLinks());
                                int realLinksUnion = compare.linkUnionSize(network1.getRealLinks(),network2.getRealLinks());
                                double realLinksJaccardIndex = compare.jaccardIndex(realLinksOverlap,realLinksUnion);

                                int virtualNodesOverlap =
                                        compare.nodeInteresectionSize(network1.getVirtualNodes(),network2.getVirtualNodes());
                                int virtualNodesUnion =
                                        compare.nodeUnionSize(network1.getVirtualNodes(),network2.getVirtualNodes());
                                double virtualNodesJaccardIndex = compare.jaccardIndex(virtualNodesOverlap, virtualNodesUnion);

                                int virtualLinksOverlap =
                                        compare.linkInteresectionSize(network1.getVirtualLinks(),network2.getVirtualLinks());
                                int virtualLinksUnion =
                                        compare.linkUnionSize(network1.getVirtualLinks(),network2.getVirtualLinks());
                                double virtualLinksJaccardIndex = compare.jaccardIndex(virtualLinksOverlap, virtualLinksUnion);

                                String body = network1.getIdentifier() + "\t" + network2.getIdentifier() + "\t" +
                                        network1.getGETObject().getTaxonomies().get(0).getSpecies() + "\t" + network2.getGETObject().getTaxonomies().get(0).getSpecies() + "\t" +
                                        linkCosSimVT + "\t" + linkCosSimVF + "\t" +
                                        propCosSimVT + "\t" + propCosSimVF + "\t" +

                                        network1.realNodesNumber() + "\t" + network2.realNodesNumber() + "\t" +
                                        network1.linksNumberVF() + "\t" + network2.linksNumberVF() +"\t" +
                                        realNodesOverlap + "\t" + realNodesJaccardIndex + "\t" +
                                        realLinksOverlap + "\t" + realLinksJaccardIndex + "\t" +

                                        network1.getNetworkDensityVT() + "\t" + network2.getNetworkDensityVT() + "\t" +
                                        densityDiffVT + "\t" + densityRatioVT + "\t" +
                                        network1.getNetworkDensityVF() + "\t" + network2.getNetworkDensityVF() + "\t" +
                                        densityDiffVF + "\t" + densityRatioVF + "\t" +

                                        network1.virtualNodesNumber() + "\t" + network2.virtualNodesNumber() + "\t" +
                                        network1.virtualLinksNumber() + "\t" + network2.virtualLinksNumber() + "\t" +

                                        virtualNodesOverlap + "\t" + virtualNodesJaccardIndex + "\t" +
                                        virtualLinksOverlap + "\t" + virtualLinksJaccardIndex + "\t" +

                                        network1.getLinkStringVT() + "\t" + network2.getLinkStringVT() + "\t" +
                                        network1.getLinkStringVF() + "\t" + network2.getLinkStringVF() + "\t" +

                                        network1.getLinkTransitionsVT() + "\t" + network1.getLinkTransitionsVF() + "\t" +
                                        network2.getLinkTransitionsVT() + "\t" + network2.getLinkTransitionsVF() + "\t" +

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
        System.out.println("File '"+fileName+"' has been created in directory '"+targetDirectory+"'");
    }
}
