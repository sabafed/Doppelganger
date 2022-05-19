package org.expasy.glyconnect.doppelganger.doppelganger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Main class.
 *
 * Its purpose is to obtain a doppelganger object for each file in a directory.
 */
public class reader {
    public static void main(String[] args) throws Exception {
        ArrayList<doppelganger> gangers = new ArrayList<>(readfiles("proteinsAll"));

        for (int i = 0; i < gangers.size(); i++) {
            System.out.println( gangers.get(i).getIdentifier() );
            gangers.get(i).getPOSTObject().attributesChecker();
        }
    }

    public static ArrayList<doppelganger> readfiles(String sourceDirectory) throws Exception {
        ArrayList<doppelganger> gangers = new ArrayList<>();
        File directory = new File("/home/federico/Documenti/Thesis/Doppelganger/"+sourceDirectory+"/N-Linked");;
        File[] files = directory.listFiles();

        int totalDoiless = 0;

        for (File file : files) {
            Path doiJson = Path.of(String.valueOf(file));
            doppelganger doppel = new doppelganger(doiJson);

            gangers.add(doppel);
            totalDoiless += doppel.doiless;
            //System.out.println(doppel.getIdentifier()+"\n___________________________________________________________________");
        }
        return gangers;
    }
}
