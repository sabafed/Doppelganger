package org.expasy.glyconnect.doppelganger.doppelganger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class.
 *
 * Its purpose is to obtain a doppelganger object for each file in a directory.
 */
public class reader {
    public static void main(String[] args) throws Exception {
        List<doppelganger> gangers = new ArrayList<>(readfiles());

        for (int i = 0; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if (i != j) {
                    doppelganger dpg1 = gangers.get(i);
                    doppelganger dpg2 = gangers.get(j);

                    if ( dpg1.equals(dpg2) ) System.out.println(gangers.get(i).getDoi());
                }
            }

        }
    }

    public static List<doppelganger> readfiles() throws Exception {
        List<doppelganger> gangers = new ArrayList<>();
        File directory = new File("/home/federico/Documenti/Thesis/Doppelganger/referenceDB/O-Linked");
        File[] files = directory.listFiles();

        int totalDoiless = 0;

        for (File file : files) {
            Path doiJson = Path.of(String.valueOf(file));
            doppelganger doppel = new doppelganger(doiJson);

            gangers.add(doppel);
            totalDoiless += doppel.doiless;
            //System.out.println(doppel.getDoi()+"\n___________________________________________________________________");
        }
        return gangers;
    }
}
