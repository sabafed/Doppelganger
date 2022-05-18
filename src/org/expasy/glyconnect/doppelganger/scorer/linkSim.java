package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.link;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.node;
import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Link similarity class.
 * Extract the information on the networks (doppelganger objects),
 * computes calculation on the count of the 14 link types in the database,
 * is the subject of the comparison class.
 */
public class linkSim {
    private String identifier;
    private final doppelganger doppelganger;
    private ArrayList<String> realNodes = new ArrayList<String>();
    private ArrayList<String> virtualNodes = new ArrayList<String>();
    private String linkStringVT; // String of all the links in the doppelganger object, virtuals included.
    private String linkStringVF; // String of all the links in the doppelganger object, virtuals excluded.
    private HashMap<Character, Integer> linkCountVT = new HashMap<Character, Integer>(); // Counts the occurrences of each link in the network, virtuals included.
    private HashMap<Character, Integer> linkCountVF = new HashMap<Character, Integer>(); // Counts the occurrences of each link in the network, virtuals excluded.
    private HashMap<Character, Double> linkFreqVT = new HashMap<Character, Double>(); // Link frequencies relative to the total number of links in the network, virtuals included;
    private HashMap<Character, Double> linkFreqVF = new HashMap<Character, Double>(); // Link frequencies relative to the total number of links in the network, virtuals excluded;

    /**
     * Main constructor
     *
     * @param doppelganger A doppelganger object from which to extract network information.
     */
    public linkSim(doppelganger doppelganger) {
        this.identifier = doppelganger.getIdentifier();
        this.doppelganger = doppelganger;

        this.setNodesArrays();
        this.setLinkStrings();

        this.linkCountVT = countLinks(this.linkStringVT);
        this.linkCountVF = countLinks(this.linkStringVF);

        this.linkFreqVT = linkFrequncies(this.linkCountVT);
        this.linkFreqVF = linkFrequncies(this.linkCountVF);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ArrayList<String> getRealNodes() {
        return realNodes;
    }

    public ArrayList<String> getVirtualNodes() {
        return virtualNodes;
    }

    /* Fills realNodes and virtualNodes from doppelganger.POSTObject.nodes */
    public void setNodesArrays() {
        Map<Integer, node> nodes = this.doppelganger.getPOSTObject().getNodes();

        ArrayList<String> real = new ArrayList<String>();
        ArrayList<String> virtual = new ArrayList<String>();

        for (Integer i : nodes.keySet()) {
            if ( !(nodes.get(i).isVirtual()) && !(real.contains(nodes.get(i).toString())) )
                real.add(nodes.get(i).toString());
            else if ( nodes.get(i).isVirtual() && !(virtual.contains(nodes.get(i).toString())) )
                virtual.add(nodes.get(i).toString());
        }
        if ( real.size()    != 0 ) this.realNodes = real;
        if ( virtual.size() != 0 ) this.virtualNodes = virtual;
    }

    public String getLinkStringVT() {
        return linkStringVT;
    }

    public String getLinkStringVF() {
        return linkStringVF;
    }

    /* Composes both linkStringVT and linkStringVF */
    public void setLinkStrings() {
        Map<Integer, link> links = this.doppelganger.getPOSTObject().getLinks();
        StringBuilder linksVT = new StringBuilder();
        StringBuilder linksVF = new StringBuilder();
        linksVT.append("");
        linksVF.append("");

        for (Integer i : links.keySet()) {
            linksVT.append(links.get(i).getCondensedFormat()); //VT takes both virtual and real links

            if ( !(this.virtualNodes.contains(links.get(i).getSource())) && !(this.virtualNodes.contains(links.get(i).getTarget())) )
                linksVF.append(links.get(i).getCondensedFormat()); //VF takes only links between two real nodes
        }

        this.linkStringVT = linksVT.toString();
        this.linkStringVF = linksVF.toString();
    }

    public HashMap<Character, Integer> getLinkCountVT() {
        return linkCountVT;
    }

    public HashMap<Character, Integer> getLinkCountVF() {
        return linkCountVF;
    }

    public HashMap<Character, Integer> countLinks(String linkString) {
        HashMap<Character,Integer> counts = new HashMap<Character,Integer>();

        // Residues, and therefore links, can be of 13 types + 1 X for "others".
        String residues = "HNFSGPAKOampsX";

        for (int f = 0; f < residues.length(); f++) {
            char res = residues.charAt(f);
            counts.put(res,0);
        }

        // Counting types:
        for (int s = 0; s < linkString.length(); s++) {
            if (counts.containsKey(linkString.charAt(s))) {
                counts.replace(linkString.charAt(s), counts.get(linkString.charAt(s))+1);
            } else {
                System.out.println("ERROR: "+linkString.charAt(s)+" - Not a glycan type.");
                System.out.println("       Identifier: "+identifier);
                System.out.println("       Glycan composition: "+linkString);
                System.exit(1);
            }
        }
        return counts;
    }

    public HashMap<Character, Double> getLinkFreqVT() {
        return linkFreqVT;
    }

    public HashMap<Character, Double> getLinkFreqVF() {
        return linkFreqVF;
    }

    public HashMap<Character, Double> linkFrequncies(HashMap<Character, Integer> linkCount) {
        HashMap <Character, Double> linkFrequncies = new HashMap<Character, Double>();

        double totalLinks = 0.0;

        for (Character residue : linkCount.keySet()) {
            linkFrequncies.put(residue, 0.0);
            totalLinks += Double.valueOf(linkCount.get(residue));
        }

        for (Character residue : linkFrequncies.keySet()) {
            if ( totalLinks != 0.0 )
                linkFrequncies.replace(residue, Double.valueOf(linkCount.get(residue))/totalLinks);
        }
        return linkFrequncies;
    }

    /* This function is used to call comparison.cosineSimilarity() */
    public double[] plainFrequencies(HashMap<Character, Double> linkFreq) {
        // Order in HashMap: A, a, F, G, H, K, m, N, O, P, p, S, s, X
        double[] frequencies = new double[14];
        int pos = 0;
        for (char residue : linkFreq.keySet()) {
            frequencies[pos] = linkFreq.get(residue);
            pos++;
        }
        return frequencies;
    }

    public int realNodesNumber() {
        return this.realNodes.size();
    }

    public int virtualNodesNumber() {
        return this.virtualNodes.size();
    }

    public int linkNumberVT() {
        return linkStringVT.length();
    }

    public int linkNumberVF() {
        return linkStringVF.length();
    }

    @Override
    public String toString() {
        return this.identifier;
    }
}
