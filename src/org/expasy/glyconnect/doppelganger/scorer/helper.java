package org.expasy.glyconnect.doppelganger.scorer;

import java.util.HashMap;

/**
 * Helper class.
 * Extract the information on the networks (doppelganger objects),
 * computes calculation on the count of the link types and properties in the database,
 * sets Doppelganger's attributes for link similarity and property profiles scores.
 */
public class helper {
    public static HashMap<Character, Integer> countLinks(String linkString) {
        HashMap<Character,Integer> counts = new HashMap<>();

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
                //System.out.println("       Identifier: "+identifier);
                System.out.println("       Glycan composition: "+linkString);
                System.exit(1);
            }
        }
        return counts;
    }

    private static HashMap<Character, Integer> propFreqAdapter(HashMap<String, Integer> propertiesCount) {
        HashMap <Character, Integer> charCounts = new HashMap<>();

        for (String property : propertiesCount.keySet()) {
            if ( property.equals("Neutral") )
                charCounts.put('N', propertiesCount.get(property));
            else if ( property.equals("Fucosylated") )
                charCounts.put('F', propertiesCount.get(property));
            else if ( property.equals("Sialylated") )
                charCounts.put('S', propertiesCount.get(property));
            else if ( property.equals("Fuco-sialylated") )
                charCounts.put('U', propertiesCount.get(property));
            else if ( property.equals("Oligomannose") )
                    charCounts.put('O', propertiesCount.get(property));
            else if ( property.equals("Sulfated") )
                    charCounts.put('L', propertiesCount.get(property));
        }
        return charCounts;
    }

    public static HashMap<String, Double> propertiesFrequencies(HashMap<String, Integer> propertiesCount, int nodesNumber) {
        HashMap <String, Double> propertiesFrequencies = new HashMap<>();

        for (String property : propertiesCount.keySet())
            propertiesFrequencies.put(property, 0.0);

        HashMap<Character, Double> frequencies = computeFrequencies( propFreqAdapter(propertiesCount) , nodesNumber);
        //System.out.println("\n"+frequencies);

        for (char prop : frequencies.keySet()) {
            switch ( prop ) {
                case 'N':
                    propertiesFrequencies.replace("Neutral", frequencies.get('N'));
                case 'F':
                    propertiesFrequencies.replace("Fucosylated", frequencies.get('F'));
                case 'S':
                    propertiesFrequencies.replace("Sialylated", frequencies.get('S'));
                case 'U':
                    propertiesFrequencies.replace("Fuco-sialylated", frequencies.get('U'));
                case 'O':
                    propertiesFrequencies.replace("Oligomannose", frequencies.get('O'));
                case 'L':
                    propertiesFrequencies.replace("Sulfated", frequencies.get('L'));
            }
        }
        //System.out.println(propertiesFrequencies);
        return propertiesFrequencies;
    }

    public static HashMap<Character, Double> linkFrequencies(HashMap<Character, Integer> linkCount, int linksNumber) {
        return computeFrequencies(linkCount, linksNumber);
    }

    private static HashMap<Character, Double> computeFrequencies(HashMap<Character, Integer> count, int denominator) {
        HashMap <Character, Double> frequencies = new HashMap<>();

        double total = (double) denominator; //0.0;

        for ( Character residue : count.keySet()) {
            if ( total != 0.0 ) frequencies.put(residue, Double.valueOf( count.get(residue) ) / total );
            else frequencies.put(residue, 0.0);
        }

        /*
        for (Character residue : count.keySet()) {
            frequencies.put(residue, 0.0);
            total += Double.valueOf(count.get(residue));
        }

        for (Character residue : frequencies.keySet()) {
            if ( total != 0.0 )
                frequencies.replace(residue, Double.valueOf(count.get(residue))/ total);
        }
        */

        return frequencies;
    }

    /* This function is used to call compare.cosineSimilarity() */
    public static double[] frequenciesAsDouble(HashMap<Character, Double> frequenciesMap) {

         /* Order in HashMap<Character, Double> linkFreq:
          *     A, a, F, G, H, K, m, N, O, P, p, S, s, X
          */

        double[] frequencies = new double[frequenciesMap.size()];
        int pos = 0;
        for (char residue : frequenciesMap.keySet()) {
            frequencies[pos] = frequenciesMap.get(residue);
            pos++;
        }
        return frequencies;
    }
}
