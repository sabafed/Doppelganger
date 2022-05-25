package org.expasy.glyconnect.doppelganger.scorer;

import java.util.ArrayList;

public class compare {
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        //for (int i = 0; i < vectorA.length; i++) System.out.print(vectorA[i]+" ");
        double dotProduct = 0.0;
        double normA      = 0.0;
        double normB      = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static double networkDensity(int nodesNumber, int linksNumber) {
        double nn = (double) nodesNumber;
        double ln = (double) linksNumber;

        double density = ( 2 * ln / (nn * (nn - 1)) ) * 100;

        if ( Double.isNaN(density) ) return 0.0;
        return density;
    }

    public static double densityDifference(double densityA, double densityB) {
        return Math.abs(densityA - densityB);
    }

    public double jaccardIndex(ArrayList<String> setA, ArrayList<String> setB) {
        ArrayList<String> AintersectionB = new ArrayList<>();
        ArrayList<String> AunionB        = new ArrayList<>();

        for (int f = 0; f < setA.size(); f++) {
            for (int s = 0; s < setB.size(); s++) {
                String sA = setA.get(f);
                String sB = setB.get(s);

                // Union is the set including elements present in AT LEAST ONE of the original sets ( = the set of all the elements)
                if ( !(sA.equals(sB)) ) {
                    if ( !AunionB.contains(sA) ) AunionB.add(sA);
                    if ( !AunionB.contains(sB) ) AunionB.add(sB);
                }

                // Intersection is the set including elements present on BOTH original sets
                if(sA.equals(sB)) {
                    if ( !AintersectionB.contains(sA) ) AintersectionB.add(sA);
                    if ( !AunionB.contains(sA) )        AunionB.add(sA);
                }
            }
        }

        // Jaccard index is the size of the intersection over the size of the union of two sets
        double jaccardIndex = (double)AintersectionB.size() / (double)AunionB.size();

        return jaccardIndex * 100;
    }
}
