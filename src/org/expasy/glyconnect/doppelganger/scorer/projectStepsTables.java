package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class projectStepsTables {
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

        ArrayList<doppelganger> networks = reader.readfiles(sourceDirectory, glycanType);
        //for (doppelganger doppel : networks) System.out.println(doppel.getIdentifier()+" : "+doppel.nodesNumberVF());
        //for (doppelganger doppel : networks) doppel.attributesChecker();
        jaccardIndexToTable(glycanType, networks, sourceDirectory);
    }

    public static void jaccardIndexToTable(String glycanType, ArrayList<doppelganger> networks, String sourceDirectory) throws FileNotFoundException {
        int minNetworkSize = 5;
        double minJIScore = 0.600000;

        //sourcesAll folder contains subfolders tha could get in the way of file saving
        sourceDirectory = sourceDirectory.replace("/", "_");
        String targetDirectory = "results/";
        String fileName = sourceDirectory + "_" + glycanType + "_minSize" + minNetworkSize + "_JaccardIndex";
        File outFile = new File(targetDirectory + fileName + "_TEST_" + ".tsv");
        PrintStream output = new PrintStream(outFile);
        PrintStream console = System.out;
        System.setOut(output);

        String header = "Network A" + "\t" + "Network B" + "\t" +
                "Taxonomy A" + "\t" + "Taxonomy B" + "\t" +

                "Nodes Number A (Virtual F)" + "\t" + "Nodes Number B (Virtual F)" + "\t" +
                "Links Number A (Virtual F)" + "\t" + "Links Number B (Virtual F)" + "\t" +
                "Real Nodes Overlap" + "\t" + "Real Nodes Union" + "\t" + "Real Nodes Jaccard Index" + "\t" +
                "Real Links Overlap" + "\t" + "Real Links Union" + "\t" + "Real Links Jaccard Index" + "\t" +

                "Virtual Nodes Number A" + "\t" + "Virtual Nodes Number B" + "\t" +
                "Virtual Links Number A" + "\t" + "Virtual Links Number B" + "\t" +

                "Virtual Nodes Overlap" + "\t" + "Virtual Nodes Union" + "\t" + "Virtual Nodes Jaccard Index" + "\t" +
                "Virtual Links Overlap" + "\t" + "Virtual Links Union" + "\t" + "Virtual Links Jaccard Index" + "\t" +

                "Link counts A (virtual T)" + "\t" + "Link counts B (virtual T)" + "\t" +
                "Link counts Intersection (virtual T)" + "\t" +
                "Link counts Union (virtual T)" + "\t" +
                "Link counts Jaccard Index (virtual T)" + "\t" +
                "Frequencies A (virtual T)" + "\t" + "Frequencies B (virtual T)" + "\t" +

                "Link counts A (virtual F)" + "\t" + "Link counts B (virtual F)" + "\t" +
                "Link counts Intersection (virtual F)" + "\t" +
                "Link counts Union (virtual F)" + "\t" +
                "Link counts Jaccard Index (virtual F)" + "\t" +
                "Frequencies A (virtual F)" + "\t" + "Frequencies B (virtual F)";

        System.out.println(header);

        for (int f = 0; f < networks.size(); f++) {
            for (int s = 0; s < networks.size(); s++) {
                if (!networks.get(f).getIdentifier().equals(networks.get(s).getIdentifier())) {
                    if ( networks.get(f).realNodesNumber() > minNetworkSize && networks.get(s).realNodesNumber() > minNetworkSize) {
                        doppelganger network1 = networks.get(f);
                        doppelganger network2 = networks.get(s);

                        int realNodesOverlap =
                                compare.nodeInteresectionSize(network1.getRealNodes(), network2.getRealNodes());
                        int realNodesUnion =
                                compare.nodeUnionSize(network1.getRealNodes(), network2.getRealNodes());
                        double realNodesJaccardIndex = compare.jaccardIndex(realNodesOverlap, realNodesUnion);

                        int realLinksOverlap = compare.linkInteresectionSize(network1.getRealLinks(), network2.getRealLinks());
                        int realLinksUnion = compare.linkUnionSize(network1.getRealLinks(), network2.getRealLinks());
                        double realLinksJaccardIndex = compare.jaccardIndex(realLinksOverlap, realLinksUnion);

                        int virtualNodesOverlap =
                                compare.nodeInteresectionSize(network1.getVirtualNodes(), network2.getVirtualNodes());
                        int virtualNodesUnion =
                                compare.nodeUnionSize(network1.getVirtualNodes(), network2.getVirtualNodes());
                        double virtualNodesJaccardIndex = compare.jaccardIndex(virtualNodesOverlap, virtualNodesUnion);

                        int virtualLinksOverlap =
                                compare.linkInteresectionSize(network1.getVirtualLinks(), network2.getVirtualLinks());
                        int virtualLinksUnion =
                                compare.linkUnionSize(network1.getVirtualLinks(), network2.getVirtualLinks());
                        double virtualLinksJaccardIndex = compare.jaccardIndex(virtualLinksOverlap, virtualLinksUnion);

                        if ( realNodesJaccardIndex > minJIScore ||
                                realLinksJaccardIndex > minJIScore ||
                                    virtualNodesJaccardIndex > minJIScore ||
                                        virtualLinksJaccardIndex > minJIScore ) {
                            String body = network1.getIdentifier() + "\t" + network2.getIdentifier() + "\t" +
                                    network1.getGETObject().getTaxonomies().get(0).getSpecies() + "\t" + network2.getGETObject().getTaxonomies().get(0).getSpecies() + "\t" +

                                    network1.realNodesNumber() + "\t" + network2.realNodesNumber() + "\t" +
                                    network1.linksNumberVF() + "\t" + network2.linksNumberVF() + "\t" +
                                    realNodesOverlap + "\t" + realNodesUnion + "\t" +  realNodesJaccardIndex + "\t" +
                                    realLinksOverlap + "\t" + realLinksUnion + "\t" + realLinksJaccardIndex + "\t" +

                                    network1.virtualNodesNumber() + "\t" + network2.virtualNodesNumber() + "\t" +
                                    network1.virtualLinksNumber() + "\t" + network2.virtualLinksNumber() + "\t" +

                                    virtualNodesOverlap + "\t" + virtualNodesOverlap + "\t" + virtualNodesJaccardIndex + "\t" +
                                    virtualLinksOverlap + "\t" + virtualLinksUnion + "\t" + virtualLinksJaccardIndex + "\t" +

                                    network1.getLinkCountVT() + "\t" + network2.getLinkCountVT() + "\t" +
                                    compare.linkCountsIntersection(network1.getLinkCountVT(), network2.getLinkCountVT()) + "\t" +
                                    compare.linkCountsUnion(network1.getLinkCountVT(), network2.getLinkCountVT()) + "\t" +

                                    compare.jaccardIndex(compare.linkCountsIntersectionSize(network1.getLinkCountVT(), network2.getLinkCountVT()),
                                            compare.linkCountsUnionSize(network1.getLinkCountVT(), network2.getLinkCountVT())) + "\t" +

                                    network1.getLinkFreqVT() + "\t" + network2.getLinkFreqVT() + "\t" +

                                    network1.getLinkCountVF() + "\t" + network2.getLinkCountVF() + "\t" +
                                    compare.linkCountsIntersection(network1.getLinkCountVF(), network2.getLinkCountVF()) + "\t" +
                                    compare.linkCountsUnion(network1.getLinkCountVF(), network2.getLinkCountVF())  + "\t" +

                                    compare.jaccardIndex(compare.linkCountsIntersectionSize(network1.getLinkCountVF(), network2.getLinkCountVF()),
                                            compare.linkCountsUnionSize(network1.getLinkCountVF(), network2.getLinkCountVF())) + "\t" +

                                    network1.getLinkFreqVF() + "\t" + network2.getLinkFreqVF() + "\t";

                            System.out.println(body);
                        }
                    }
                }
            }
        }
        System.setOut(console);
        System.out.println("File '" + fileName + "' has been created in directory '" + targetDirectory + "'");
    }
}
