package org.expasy.glyconnect.doppelganger.virtualsImpact;

import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.node;
import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

/**
 * This class produces data summaries to understand the impact of virtual nodes on the results.
 */
public class virtualsImpact {
    public static void main(String[] args) throws Exception {

        String sourceDirectory = "proteinsAll";
        //String sourceDirectory = "sourcesAll/tissue";

        String glycanType = "N-Linked";
        //String glycanType = "O-Linked";

        ArrayList<doppelganger> gangers = reader.readfiles(sourceDirectory, glycanType);

        /*
         * ArrayList references is used to determine which virtual nodes exists in the database as experimentally proven
         * and which are created from Compozitor without appearing in the literature.
         */
        ArrayList<doppelganger> references = reader.readfiles("referencesAll", glycanType);

        virtualsImpact(gangers,references);
        //dataSummaryToTable(sourceDirectory, glycanType, gangers);
    }

    public static void dataSummaryToTable(String sourceDirectory, String glycanType, ArrayList<doppelganger> gangers) throws FileNotFoundException {
        sourceDirectory = sourceDirectory.replace("/", "_");
        String fileName = sourceDirectory+"_"+glycanType+"_DataSummary";
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

        // TODO: 27/07/22 make a script to return transitions table for VF and VT.
        /*
        for ( String k : allTransitionsVF.keySet() ) System.out.print("\""+k+"\""+",");

        System.out.println();

        for ( String k : allTransitionsVF.keySet() ) System.out.print(allTransitionsVF.get(k)+",");

        */
    }

    /**
     * In this function, all the networks of referencesAll (i.e. published literature) are parsed and all real nodes are extracted.
     * From another dataset, be it proteinsAll or sourcesAll/tissue in this case (but can work with every Compozitor dataset),
     * all the networks are parsed and virtual nodes are extracted.
     * The list of all the real nodes in Compozitor is then compared to the list of all virtual nodes in a dataset.
     * Hence, it is possible to determine which are the nodes marked as 'virtual' are actually reported in the literature,
     * and which are the ones that exist only in Compozitor.
     *
     * @param gangers whichever ArrayList<doppelganger>.
     * @param references ArrayList<doppelganger> representation of referencesAll dataset.
     * @throws Exception
     */
    public static void virtualsImpact(ArrayList<doppelganger> gangers, ArrayList<doppelganger> references) throws Exception {
        // {realNode,[networks containing realNode]}
        HashMap<node,Set<doppelganger>> realNodesAll = new HashMap<>();

        // {virtualNode,[networks containing virtualNode]}
        HashMap<node,Set<doppelganger>> virtualNodesAll = new HashMap<>();

        Set<String> virtualsCreated  = new HashSet<>();
        Set<String> virtualsExisting = new HashSet<>();

        // Populate realNodesAll with all the real nodes in referencesAll dataset
        for (doppelganger ref : references) {
            for (node real : ref.getRealNodes()) {
                realNodesAll.computeIfAbsent(real, k -> new HashSet<doppelganger>());
                // If the paper containing the node is not in the [network containing realNode],
                // then add it. Check is automated by the HashSet
                realNodesAll.get(real).add(ref);
            }
        }

        // Populate virtualNodesAll with all virtual nodes in gangers (proteinsAll and sourcesAll/tissue to begin with)
        for (doppelganger doppel : gangers) {
            for (node virtual : doppel.getVirtualNodes()) {
                virtualNodesAll.computeIfAbsent(virtual, k -> new HashSet<doppelganger>());

                // If the paper containing the node is not in the [network containing virtualNode],
                // then add it. Check is automated by the HashSet
                virtualNodesAll.get(virtual).add(doppel);
            }
        }

        for (node real : realNodesAll.keySet()){
            for (node virtual : virtualNodesAll.keySet()){
                if ( real.getCondensedFormat().equals(virtual.getCondensedFormat()) ) {
                    virtualsExisting.add(virtual.getCondensedFormat());
                }
            }
        }
        for (node real : realNodesAll.keySet()) {
            for (node virtual : virtualNodesAll.keySet()) {
                if ( !real.getCondensedFormat().equals(virtual.getCondensedFormat()) ) {
                    if ( !virtualsExisting.contains(virtual.getCondensedFormat()) )
                        virtualsCreated.add(virtual.getCondensedFormat());
                }
            }
        }

        System.out.println("Real nodes in referencesAll dataset:         "+realNodesAll.size());
        System.out.println("Virtual nodes in proteinsAll dataset:        "+virtualNodesAll.size());

        System.out.println("Virtual nodes proven to exist in literature (virtualsExisting):   " + virtualsExisting.size());
        System.out.println("Virtual nodes found EXCLUSIVELY in proteinsAll (virtualsCreated): " + virtualsCreated.size());

        virtualsToTable(virtualNodesAll, realNodesAll, virtualsCreated, virtualsExisting, "Existing");
        virtualsToTable(virtualNodesAll, realNodesAll, virtualsCreated, virtualsExisting, "Created");
    }

    public static HashMap<String,Set<doppelganger>>  virtualsMap(HashMap<node,Set<doppelganger>> nodesAll, Set<String> virtuals) {
        HashMap<String,Set<doppelganger>> virtualsMap = new HashMap<>();

        for (node realNode : nodesAll.keySet()) {
            String real = realNode.getCondensedFormat();

            for (String virtual : virtuals) {
                if ( virtual.equals(real) ) {
                    virtualsMap.computeIfAbsent(real, k -> new HashSet<doppelganger>());
                    virtualsMap.get(real).addAll(nodesAll.get(realNode));
                }
            }
        }

        return virtualsMap;
    }

    /** Creates a table in order to investigate which virtual nodes are found in literature and trace their papers,
     *  if the virtuals type is 'existing', or just investigate in which networks the created virtuals are found.
     *  Table is composed as follows:
     *                                                                         (if 'existing')
     *     header: |       node      | occurrences | present in network(s) | cited in paper(s) |
     *     body:   | condensedFormat |      Y      |     identifier(s)     |       doi(s)      |
     */
    public static void virtualsToTable(HashMap<node, Set<doppelganger>> virtualNodesAll,
                                       HashMap<node, Set<doppelganger>> realNodesAll,
                                       Set<String> virtualsCreated,
                                       Set<String> virtualsExisting,
                                       String virtualsType) throws FileNotFoundException {

        String targetDirectory = "results/virtualsImpact/";
        String fileName = ( "virtuals"+virtualsType+".tsv");
        File outFile = new File(targetDirectory+fileName);
        PrintStream output = new PrintStream(outFile);
        PrintStream console = System.out;

        System.setOut(output);

        String header = "Node" + "\t" + "Occurrences" + "\t" + "Present in network(s)" ;

        HashMap<String,Set<doppelganger>> createdMap = virtualsMap(virtualNodesAll, virtualsCreated); // retrieve virtuals present as virtuals in networks

        HashMap<String,Set<doppelganger>> existingMap  = virtualsMap(virtualNodesAll, virtualsExisting); // retrieve reals present as virtuals in networks
        HashMap<String,Set<doppelganger>> existingDOIs = new HashMap<>();

        String body = new String();

        if ( virtualsType.equals("Existing") ) {
            header += "\t" + "Cited in paper(s)";
            existingDOIs = virtualsMap(realNodesAll, virtualsExisting); // retrieve doi of the paper in which the exisisting virtuals are cited}
            for (String node : existingMap.keySet()){
                body += node + "\t" + existingMap.get(node).size() + "\t" + existingMap.get(node) + "\t" + existingDOIs.get(node);
            }
        } else {
            for (String node : createdMap.keySet()){
                body += node + "\t" + createdMap.get(node).size() + "\t" + createdMap.get(node);
            }
        }

        System.setOut(console);
        System.out.println("File '" + fileName + "' has been created in directory '" + targetDirectory + "'");
    }
}
