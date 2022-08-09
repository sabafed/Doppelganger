package org.expasy.glyconnect.doppelganger.QA;

/**
 * Statistics class contains functions necessary to perform a quality analysis.
 */
public class statistics {
    public static void main(String[] args) {
        int TP = 100;
        int FN = 50;
        int TN = 2000;
        int FP = 200;

        System.out.println("TPR: "+truePositiveRate(TP,FN) +
                "\nTNR: " + trueNegativeRate(TN,FP) +
                "\nPPV: " + positivePredictiveValue(TP,FP) +
                "\nNPV: " + negativePredictiveValue(TN,FN) );

    }
    // Senitivity:
    public static double truePositiveRate(int truePositives, int falseNegatives) {
        if ( truePositives == 0 && falseNegatives ==0 ) return 0.0;
        if ( truePositives < 0 || falseNegatives < 0 ) {
            System.out.println("Unacceptable negative value:"+
                    "\nTrue  Positives: "+truePositives+
                    "\nFalse Negatives: "+falseNegatives);
            System.exit(1);
        }
        return ( (double) truePositives / ((double) truePositives + (double) falseNegatives) );
    }

    // Specificity:
    public static double trueNegativeRate(int trueNegatives, int falsePositives) {
        if ( trueNegatives == 0 && falsePositives ==0 ) return 0.0;
        if ( trueNegatives < 0 || falsePositives < 0 ) {
            System.out.println("Unacceptable negative value:"+
                    "\nTrue  Negatives: "+trueNegatives+
                    "\nFalse Positives: "+falsePositives);
            System.exit(1);
        }
        return ( (double) trueNegatives / ((double) trueNegatives + (double) falsePositives) );
    }

    // Precision:
    public static double positivePredictiveValue(int truePositives, int falsePositives) {
        if ( truePositives == 0 && falsePositives ==0 ) return 0.0;
        if ( truePositives < 0 || falsePositives < 0 ) {
            System.out.println("Unacceptable negative value:"+
                    "\nTrue  Positives: "+truePositives+
                    "\nFalse Positives: "+falsePositives);
            System.exit(1);
        }
        return ( (double) truePositives / ((double) truePositives + (double) falsePositives) );
    }

    public static double negativePredictiveValue(int trueNegatives, int falseNegatives) {
        if ( trueNegatives == 0 && falseNegatives ==0 ) return 0.0;
        if ( trueNegatives < 0 || falseNegatives < 0 ) {
            System.out.println("Unacceptable negative value:"+
                    "\nTrue  Negatives: "+trueNegatives+
                    "\nFalse Negatives: "+falseNegatives);
            System.exit(1);
        }
        return ( (double) trueNegatives / ((double) trueNegatives + (double) falseNegatives) );
    }
}
