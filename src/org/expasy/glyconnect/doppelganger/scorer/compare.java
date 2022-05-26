package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.link;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.node;

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

        for (node nodeA : setA) {
            if ( !AunionB.contains(nodeA) ) AunionB.add(nodeA);
        }

        for (node nodeB : setB) {
            if ( !AunionB.contains(nodeB) ) AunionB.add(nodeB);
        }

        return AunionB;
    }

    public static int nodeUnionSize(ArrayList<node> setA, ArrayList<node> setB) {
        return nodeUnion(setA, setB).size();
    }

    public static ArrayList<link> linkIntersection(ArrayList<link> setA, ArrayList<link> setB) {
        ArrayList<link> AintersectionB = new ArrayList<>();

        for (link linkA : setA) {
            for (link linkB: setB) {
                // Intersection is the set including elements present on BOTH original sets
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

        for (link linkA : setA) {
            if ( !AunionB.contains(linkA) ) AunionB.add(linkA);
        }

        for (link linkB : setB) {
            if ( !AunionB.contains(linkB) ) AunionB.add(linkB);
        }

        return AunionB;
    }

    public static int linkUnionSize(ArrayList<link> setA, ArrayList<link> setB) {
        return linkUnion(setA, setB).size();
    }

    public static double jaccardIndex(int interectionSize, int unionSize) {
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

        // Jaccard index is the size of the intersection over the size of the union of two sets
        double jaccardIndex = (double)interectionSize / (double)unionSize;

        if ( Double.isNaN(jaccardIndex) ) return  0.0;

        return jaccardIndex * 100;
    }
}
