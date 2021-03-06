package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.link;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.node;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static double densityRatio(double densityA, double densityB) {
        if ( densityB == 0.0 ) return densityB/densityA;
        return densityA/densityB;
    }
    public static double densityDifference(double densityA, double densityB) {
        return Math.abs(densityA - densityB);
    }

    public static ArrayList<node> nodeIntersection(ArrayList<node> setA, ArrayList<node> setB) {
        ArrayList<node> AintersectionB = new ArrayList<>();

        for (node nodeA : setA) {
            for (node nodeB: setB) {
                // Intersection is the set including elements present on BOTH original sets
                if ( nodeA.equals(nodeB) ) {
                    if ( !AintersectionB.contains(nodeA) ) AintersectionB.add(nodeA);
                }
            }
        }

        return AintersectionB;
    }

    public static int nodeInteresectionSize(ArrayList<node> setA, ArrayList<node> setB) {
        return nodeIntersection(setA, setB).size();
    }

    public static ArrayList<node> nodeUnion(ArrayList<node> setA, ArrayList<node> setB) {
        // Union is the set including elements present in AT LEAST ONE of the original sets ( = the set of all the elements)
        ArrayList<node> AunionB = new ArrayList<>();

        AunionB.addAll(setA);

        AunionB.addAll(setB);

        return AunionB;
    }

    public static int nodeUnionSize(ArrayList<node> setA, ArrayList<node> setB) {
        return nodeUnion(setA, setB).size();
    }

    public static ArrayList<link> linkIntersection(ArrayList<link> setA, ArrayList<link> setB) {
        ArrayList<link> AintersectionB = new ArrayList<>();

        for (link linkA : setA) {
            for (link linkB: setB) {
                // Intersection is the set including elements present in BOTH original sets
                if ( linkA.equals(linkB) ) {
                    if ( !AintersectionB.contains(linkA) ) AintersectionB.add(linkA);
                }
            }
        }
        return AintersectionB;
    }

    public static int linkInteresectionSize(ArrayList<link> setA, ArrayList<link> setB) {
        return linkIntersection(setA, setB).size();
    }

    public static ArrayList<link> linkUnion(ArrayList<link> setA, ArrayList<link> setB) {
        // Union is the set including elements present in AT LEAST ONE of the original sets ( = the set of all the elements)
        ArrayList<link> AunionB = new ArrayList<>();

        AunionB.addAll(setA);

        AunionB.addAll(setB);

        return AunionB;
    }

    public static int linkUnionSize(ArrayList<link> setA, ArrayList<link> setB) {
        return linkUnion(setA, setB).size();
    }

    public static ArrayList<Character> linkCountsIntersection(HashMap<Character, Integer> countA, HashMap<Character, Integer> countB) {
        ArrayList<Character> union = new ArrayList<>();

        for (Character cA : countA.keySet()){
            for (Character cB: countB.keySet()) {
                if ( !(union.contains(cA)) && !(union.contains(cB)) ) {
                    if ( cA.equals(cB) ) union.add(cA);
                }
            }
        }

        return union;
    }

    public static int linkCountsIntersectionSize(HashMap<Character, Integer> countA, HashMap<Character, Integer> countB) {
        return linkCountsIntersection(countA,countB).size();
    }

    public static ArrayList<Character> linkCountsUnion(HashMap<Character, Integer> countA, HashMap<Character, Integer> countB) {
        ArrayList<Character> intersection = new ArrayList<>();

        for (Character count : countA.keySet()) {
            if ( countA.get(count) != 0 ) intersection.add(count);
        }

        for (Character count : countB.keySet()) {
            if ( countB.get(count) != 0 ) intersection.add(count);
        }

        return intersection;
    }

    public static int linkCountsUnionSize(HashMap<Character, Integer> countA, HashMap<Character, Integer> countB) {
        return linkCountsUnion(countA, countB).size();
    }

    public static double jaccardIndex(int intersectionSize, int unionSize) {
        /* Keeping the old method commented because of nostalgia
        for (int f = 0; f < setA.size(); f++) {
            for (int s = 0; s < setB.size(); s++) {
                node nodeA = setA.get(f);
                node nodeB = setB.get(s);

                // Union is the set including elements present in AT LEAST ONE of the original sets ( = the set of all the elements)
                if ( !(nodeA.equals(nodeB)) ) {
                    if ( !AunionB.contains(nodeA) ) AunionB.add(nodeA);
                    if ( !AunionB.contains(nodeB) ) AunionB.add(nodeB);
                }

                // Intersection is the set including elements present on BOTH original sets
                if ( nodeA.equals(nodeB) ) {
                    if ( !AintersectionB.contains(nodeA) ) AintersectionB.add(nodeA);
                    if ( !AunionB.contains(nodeA) )        AunionB.add(nodeA);
                }
            }
        }
       */

        if ( intersectionSize == unionSize ) return 1.0;

        double denominator =  unionSize - intersectionSize;
        // Jaccard index is the size of the intersection over the size of the union of two sets
        double jaccardIndex = (double) intersectionSize / denominator;

        if ( Double.isNaN(jaccardIndex) ) return  0.0;

        return jaccardIndex;
    }
}
