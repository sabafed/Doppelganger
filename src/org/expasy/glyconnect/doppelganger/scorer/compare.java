package org.expasy.glyconnect.doppelganger.scorer;

import java.util.ArrayList;
import java.util.List;

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

        return density;
    }

    public static double densityDifference(double densityA, double densityB) {
        return Math.abs(densityA - densityB);
    }

    public double jaccardIndex(ArrayList<String> setA, ArrayList<String> setB) {
        List<String> AintersectionB = new ArrayList<String>();
        List<String> AunionB        = new ArrayList<String>();

        for (int i = 0; i < setA.size(); i++) {
            for (int j = 0; j < setB.size(); j++) {
                String sA = setA.get(i);
                String sB = setB.get(j);

                // Union is the set including elements present in AT LEAST ONE of the original sets ( = the set of all the elements)
                if ( !(sA.equals(sB)) ) {
                    AunionB.add(sA);
                    AunionB.add(sB);
                }

                // Intersection is the set including elements present on BOTH original sets
                if(sA.equals(sB)) {
                    AintersectionB.add(sA);
                    AunionB.add(sA);
                }
            }
        }

        // Jaccard index is the size of the intersection over the size of the union of two sets
        double jaccardIndex = (double)AintersectionB.size() / (double)AunionB.size();

        return jaccardIndex * 100;
    }
}
