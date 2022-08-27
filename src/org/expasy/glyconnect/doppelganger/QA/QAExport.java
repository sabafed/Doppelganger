package org.expasy.glyconnect.doppelganger.QA;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.scorer.compare;
import org.expasy.glyconnect.doppelganger.scorer.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class produces data on the quality of the results and writes them to a file.
 */
public class QAExport {
    public static void QAExport(String dataset, String glycanType,
                                String method, String score, double threshold, boolean verbose) throws Exception {
        String extension = ".txt";
        if ( !verbose ) extension = ".tsv";

        String targetDirectory = "results/evaluation/"+extension.replace(".","")+"/";
        String fileName = ( "QA_"+dataset+"_"+glycanType+"_"+score.replace(" ","-")+"_"+threshold+extension );
        File outFile = new File(targetDirectory+fileName);
        PrintStream output = new PrintStream(outFile);
        PrintStream console = System.out;

        System.setOut(output);

        HashMap<String,String> results = QAImport.importResultsTable(dataset, glycanType, method, score);

        ArrayList<String> positives = QAImport.importQAComparisons(glycanType, dataset, "Wanted");
        ArrayList<String> negatives = QAImport.importQAComparisons(glycanType, dataset, "Unwanted");

        ArrayList<String> truePositives =  new ArrayList<>(); // Positive comparisons found in the results
        ArrayList<String> falsePositives = new ArrayList<>(); // Negative comparisons found in the results

        for (String res : results.keySet()) {
            for (String comp : positives) {
                if ( comp.equals(res) ) {
                    double val = Double.parseDouble(results.get(res));

                    double upBound = 1.0 + (1.0 - threshold);

                    if ( !(truePositives.contains(comp)) ) {
                        if ( !(method.contains("Density")) && val >= threshold) {
                            truePositives.add(comp);

                            if ( verbose ) {
                                System.out.println(truePositives.size() + " - wanted comparison: " + comp +
                                        "\nretrieved with method: " + method +
                                        "\nscore: " + results.get(res) + "\n");
                            }
                        }
                        else if ( method.contains("Density") && (val >= threshold && val <= upBound) ) {
                            truePositives.add(comp);

                            if ( verbose ) {
                                System.out.println(truePositives.size() + " - wanted comparison: " + comp +
                                        "\nretrieved with method: " + method +
                                        "\nscore: >= " + threshold + " <= " + results.get(res) + " <= " + upBound + "\n");
                            }
                        }
                    }
                }
            }
        }

        for (String res : results.keySet()) {
            for (String comp : negatives) {
                if (res.equals(comp)) {
                    double val = Double.parseDouble(results.get(res));

                    double upBound = 1.0 + (1.0 - threshold);

                    if ( !(falsePositives.contains(comp)) ) {
                        if ( !(method.contains("Density")) && val >= threshold ) {
                            falsePositives.add(comp);

                            if ( verbose ) {
                                System.out.println(falsePositives.size() + " - unwanted comparison: " + comp +
                                        "\nretrieved with method: " + method +
                                        "\nscore: " + val + "\n");
                            }
                        }
                        else if ( method.contains("Density") &&  (val >= threshold && val <= upBound) ) {
                            falsePositives.add(comp);

                            if ( verbose ) {
                                System.out.println(falsePositives.size() + " - unwanted comparison: " + comp +
                                        "\nretrieved with method: " + method +
                                        "\nscore: >= " + threshold + " <= " + val + " <= " + upBound +"\n");
                            }
                        }
                    }
                }
            }
        }

        ArrayList<String> falseNegatives = new ArrayList<>(positives); // Positive comparisons NOT found in the results
        falseNegatives.removeAll(truePositives);

        ArrayList<String> trueNegatives = new ArrayList<>(negatives); // Negative comparisons NOT found in the results
        trueNegatives.removeAll(falsePositives);

        if ( verbose ) {
            System.out.println("__________________________________________________________");
            System.out.println(
                    "Total Positive Comparisons: " + positives.size() +
                            "\nTrue  Positive Comparisons: " + truePositives.size() +
                            "\nFalse Positive Comparisons: " + falsePositives.size() +
                            "\nFalse Positives List:\n" + falsePositives);
            System.out.println("__________________________________________________________");
            System.out.println(
                    "Total Negative Comparisons: " + negatives.size() +
                            "\nTrue  Negative Comparisons: " + trueNegatives.size() +
                            "\nFalse Negative Comparisons: " + falseNegatives.size() +
                            "\nFalse Negatives List:\n" + falseNegatives);
            System.out.println("__________________________________________________________");
        } else {
            System.out.println("Total Positive Comparisons" + "\t" + "True  Positive Comparisons" + "\t" +
                            "False Positive Comparisons" + "\t" + "False Positives List" + "\t" +
                            "Total Negative Comparisons" + "\t" + "True  Negative Comparisons" + "\t" +
                            "False Negative Comparisons" + "\t" + "False Negatives List" );

            System.out.println( positives.size() + "\t" + truePositives.size() + "\t" +
                    falsePositives.size() + "\t" + falsePositives + "\t" +
                    negatives.size() + "\t" + trueNegatives.size() + "\t" +
                    falseNegatives.size() + "\t" + falseNegatives );
        }

        int TP = truePositives.size();
        int TN = trueNegatives.size();
        int FP = falsePositives.size();
        int FN = falseNegatives.size();

        statistics.statistics(TP,TN,FP,FN, verbose);

        System.setOut(console);
        System.out.println("File '" + fileName + "' has been created in directory '" + targetDirectory + "'");
    }

    public static void methodToTable(String glycanType, ArrayList<doppelganger> networks,
                                     String sourceDirectory, String method) throws FileNotFoundException {
        int minNetworkSize = 5;

        //sourcesAll folder contains subfolders that could get in the way of file saving
        sourceDirectory = sourceDirectory.replace("/", "_");
        String targetDirectory = "results/evaluation/";
        String fileName = sourceDirectory + "_" + glycanType + "_minSize" + minNetworkSize + "_" +
                method + ".tsv";
        File outFile = new File(targetDirectory + fileName );
        PrintStream output = new PrintStream(outFile);
        PrintStream console = System.out;
        System.setOut(output);

        String header = makeHeader(method);

        System.out.println(header);

        for (int f = 0; f < networks.size(); f++) {
            for (int s = 0; s < networks.size(); s++) {
                if (!networks.get(f).getIdentifier().equals(networks.get(s).getIdentifier())) {
                    if ( networks.get(f).realNodesNumber() > minNetworkSize && networks.get(s).realNodesNumber() > minNetworkSize) {
                        doppelganger network1 = networks.get(f);
                        doppelganger network2 = networks.get(s);
/*
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

 */
                            String body = QAExport.makeBody(network1, network2, method);

                            System.out.println(body);
                       // }
                    }
                }
            }
        }
        System.setOut(console);
        System.out.println("\nFile '" + fileName + "' has been created in directory '" + targetDirectory + "'");
    }
    public static String makeHeader(String method) {
        String identifiers = "Network A" + "\t" + "Network B" + "\t"+
                "Taxonomy A" + "\t" + "Taxonomy B" + "\t";

        String linkCosSim = "Link Cosine Similarity (virtual T)" + "\t" + "Link Cosine Similarity (virtual F)" + "\t";

        String profileCosSim = "Profile Cosine Similarity (virtual T)" + "\t" + "Profile Cosine Similarity (virtual F)" + "\t";

        String realNodesLinks = "Nodes Number A (Virtual F)" + "\t" + "Nodes Number B (Virtual F)" + "\t" +
                "Links Number A (Virtual F)" + "\t" + "Links Number B (Virtual F)" + "\t";

        String realJaccardIndex = "Real Nodes Overlap" + "\t" + "Real Nodes Jaccard Index" + "\t" +
                "Real Links Overlap" + "\t" + "Real Links Jaccard Index" + "\t";

        String density = "Density A (Virtual T)" + "\t" + "Density B (Virtual T)" + "\t" +
                "Density Difference (Virtual T)" + "\t" + "Density Ratio (Virtual T)" + "\t" +
                "Density A (Virtual F)" + "\t" + "Density B (Virtual F)" + "\t" +
                "Density Difference (Virtual F)" + "\t" + "Density Ratio (Virtual F)" + "\t";

        String virtualNodesLinks = "Virtual Nodes Number A" + "\t" + "Virtual Nodes Number B" + "\t" +
                "Virtual Links Number A" + "\t" + "Virtual Links Number B" + "\t";

        String virtualJaccardIndex = "Virtual Nodes Overlap" + "\t" + "Virtual Nodes Jaccard Index" + "\t" +
                "Virtual Links Overlap" + "\t" + "Virtual Links Jaccard Index" + "\t";

        String linkStrings = "Links String A (virtual T)" + "\t" + "Links String B (virtual T)" + "\t" +
                "Links String A (virtual F)" + "\t" + "Links String B (virtual F)" + "\t";

        String linkTransitions = "Link Transitions A (virtual T)" + "\t" + "Link Transitions A (virtual F)" + "\t" +
                "Link Transitions B (virtual T)" + "\t" + "Link Transitions B (virtual F)" + "\t";

        String linkCountsFreqs = "Link counts A (virtual T)" + "\t" + "Link counts B (virtual T)" + "\t" +
                "Frequencies A (virtual T)" + "\t" + "Frequencies B (virtual T)" + "\t" +

                "Link counts A (virtual F)" + "\t" + "Link counts B (virtual F)" + "\t" +
                "Frequencies A (virtual F)" + "\t" + "Frequencies B (virtual F)";

        String header = identifiers;

        switch (method) {
            case "linkCosSim" ->
                    header += linkCosSim + realNodesLinks + virtualNodesLinks + linkStrings + linkTransitions +
                            linkCountsFreqs;
            case "profileCosSim" ->
                    header += profileCosSim + realNodesLinks + virtualNodesLinks + linkStrings + linkTransitions +
                            linkCountsFreqs;
            case "JaccardIndex" ->
                    header += realNodesLinks + realJaccardIndex + virtualNodesLinks + virtualJaccardIndex + linkStrings +
                            linkTransitions + linkCountsFreqs;
            case "density" ->
                    header += realNodesLinks + virtualNodesLinks + density + linkStrings + linkTransitions + linkCountsFreqs;
            case "all" ->
                header += linkCosSim + profileCosSim + realNodesLinks+ realJaccardIndex + density + virtualNodesLinks +
                        virtualJaccardIndex + linkStrings + linkTransitions + linkCountsFreqs;
            default -> {
                System.out.println("Invalid method: " + method);
                System.exit(1);
            }
        }
        return header;
    }

    public static String makeBody(doppelganger network1, doppelganger network2, String method) {
        String identifiers = network1.getIdentifier() + "\t" + network2.getIdentifier() + "\t" +
                network1.getGETObject().getTaxonomies().get(0).getSpecies() + "\t" +
                network2.getGETObject().getTaxonomies().get(0).getSpecies() + "\t";

        double linkCosSimVT = 0.0;
        double linkCosSimVF = 0.0;

        double propCosSimVT = 0.0;
        double propCosSimVF = 0.0;

        String realNodesLinks = network1.realNodesNumber() + "\t" + network2.realNodesNumber() + "\t" +
                network1.linksNumberVF() + "\t" + network2.linksNumberVF() +"\t";

        int realNodesOverlap = 0;
        int realNodesUnion = 0;
        double realNodesJaccardIndex = 0.0;

        int realLinksOverlap = 0;
        int realLinksUnion = 0;
        double realLinksJaccardIndex = 0.0;

        String densityVT = network1.getNetworkDensityVT() + "\t" + network2.getNetworkDensityVT() + "\t" ;
        double densityDiffVT = 0.0;
        double densityRatioVT = 0.0;

        String densityVF =  network1.getNetworkDensityVF() + "\t" + network2.getNetworkDensityVF() + "\t";
        double densityDiffVF = 0.0;
        double densityRatioVF = 0.0;

        String virtualNodesLinks =  network1.virtualNodesNumber() + "\t" + network2.virtualNodesNumber() + "\t" +
                network1.virtualLinksNumber() + "\t" + network2.virtualLinksNumber() + "\t";

        int virtualNodesOverlap = 0;
        int virtualNodesUnion = 0;
        double virtualNodesJaccardIndex = 0.0;

        int virtualLinksOverlap = 0;
        int virtualLinksUnion = 0;
        double virtualLinksJaccardIndex = 0.0;

        String linkStrings = network1.getLinkStringVT() + "\t" + network2.getLinkStringVT() + "\t" +
                network1.getLinkStringVF() + "\t" + network2.getLinkStringVF() + "\t";

        String linkTransitions = network1.getLinkTransitionsVT() + "\t" + network1.getLinkTransitionsVF() + "\t" +
                network2.getLinkTransitionsVT() + "\t" + network2.getLinkTransitionsVF() + "\t";

        String linkCountsFreqs = network1.getLinkCountVT() + "\t" + network2.getLinkCountVT() + "\t" +
                network1.getLinkFreqVT() + "\t" + network2.getLinkFreqVT() + "\t" +

                network1.getLinkCountVF() + "\t" + network2.getLinkCountVF() + "\t" +
                network1.getLinkFreqVF() + "\t" + network2.getLinkFreqVF() + "\t";

        String body = identifiers;

        if ( method.equals("linkCosSim") || method.equals("all") ) {
            linkCosSimVT = compare.cosineSimilarity(
                    helper.frequenciesAsDouble(network1.getLinkFreqVT()),
                    helper.frequenciesAsDouble(network2.getLinkFreqVT()));

            linkCosSimVF = compare.cosineSimilarity(
                    helper.frequenciesAsDouble(network1.getLinkFreqVF()),
                    helper.frequenciesAsDouble(network2.getLinkFreqVF()));

            body += linkCosSimVT + "\t" + linkCosSimVF + "\t" + realNodesLinks + virtualNodesLinks
                    + linkStrings + linkTransitions + linkCountsFreqs;
        }
        if ( method.equals("profileCosSim") || method.equals("all") ) {
            propCosSimVT = compare.cosineSimilarity(
                    helper.frequenciesAsDouble(helper.freqsStringToChar(network1.getPropertiesFreqVT())),
                    helper.frequenciesAsDouble(helper.freqsStringToChar(network2.getPropertiesFreqVF())));

            propCosSimVF = compare.cosineSimilarity(
                    helper.frequenciesAsDouble(helper.freqsStringToChar(network1.getPropertiesFreqVF())),
                    helper.frequenciesAsDouble(helper.freqsStringToChar(network2.getPropertiesFreqVF())));

            body += propCosSimVT + "\t" + propCosSimVF + "\t" + realNodesLinks + virtualNodesLinks
                    + linkStrings + linkTransitions + linkCountsFreqs;
        }
        if ( method.equals("JaccardIndex") || method.equals("all") ) {
            realNodesOverlap = compare.nodeInteresectionSize(
                    network1.getRealNodes(),
                    network2.getRealNodes());
            realNodesUnion = compare.nodeUnionSize(
                    network1.getRealNodes(),
                    network2.getRealNodes());
            realNodesJaccardIndex = compare.jaccardIndex(realNodesOverlap, realNodesUnion);

            realLinksOverlap = compare.linkInteresectionSize(network1.getRealLinks(), network2.getRealLinks());
            realLinksUnion = compare.linkUnionSize(network1.getRealLinks(), network2.getRealLinks());
            realLinksJaccardIndex = compare.jaccardIndex(realLinksOverlap, realLinksUnion);

            virtualNodesOverlap = compare.nodeInteresectionSize(
                    network1.getVirtualNodes(),
                    network2.getVirtualNodes());
            virtualNodesUnion = compare.nodeUnionSize(network1.getVirtualNodes(), network2.getVirtualNodes());
            virtualNodesJaccardIndex = compare.jaccardIndex(virtualNodesOverlap, virtualNodesUnion);

            virtualLinksOverlap = compare.linkInteresectionSize(network1.getVirtualLinks(), network2.getVirtualLinks());
            virtualLinksUnion = compare.linkUnionSize(network1.getVirtualLinks(), network2.getVirtualLinks());
            virtualLinksJaccardIndex = compare.jaccardIndex(virtualLinksOverlap, virtualLinksUnion);

            body += realNodesLinks + realNodesOverlap + "\t" + realNodesJaccardIndex + "\t" +
                    realLinksOverlap + "\t" + realLinksJaccardIndex + "\t" +
                    virtualNodesLinks + virtualNodesOverlap + "\t" + virtualNodesJaccardIndex + "\t" +
                    virtualLinksOverlap + "\t" + virtualLinksJaccardIndex + "\t" +
                    linkStrings + linkTransitions + linkCountsFreqs;
        }
        if ( method.equals("density") || method.equals("all") ){
            densityDiffVT = compare.densityDifference(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());
            densityRatioVT = compare.densityRatio(network1.getNetworkDensityVT(), network2.getNetworkDensityVT());

            densityRatioVF = compare.densityRatio(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());
            densityDiffVF = compare.densityDifference(network1.getNetworkDensityVF(), network2.getNetworkDensityVF());

            body += realNodesLinks + virtualNodesLinks +
                    densityVT + densityDiffVT + "\t" + densityRatioVT + "\t" +
                    densityVF + densityDiffVF + "\t" + densityRatioVF + "\t" +
                    linkStrings + linkTransitions + linkCountsFreqs;
        }

        if ( method.equals("all") ) {
            body += linkCosSimVT + "\t" + linkCosSimVF + "\t" + propCosSimVT + "\t" + propCosSimVF + "\t" +
                    realNodesLinks + realNodesOverlap + "\t" + realNodesJaccardIndex + "\t" +
                    realLinksOverlap + "\t" + realLinksJaccardIndex + "\t" +
                    densityVT + densityDiffVT + "\t" + densityRatioVT + "\t" +
                    densityVF + densityDiffVF + "\t" + densityRatioVF + "\t" +
                    virtualNodesLinks + virtualNodesOverlap + "\t" + virtualNodesJaccardIndex + "\t" +
                    virtualLinksOverlap + "\t" + virtualLinksJaccardIndex + "\t" +
                    linkStrings + linkTransitions + linkCountsFreqs;
        }

        return body;
    }
}
