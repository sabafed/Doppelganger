package org.expasy.glyconnect.doppelganger.doppelganger;

import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.structure;

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
            for ( structure s :gangers.get(i).getGETObject().getStructures() )
                System.out.println(s.getStructureJson());
        }

/*
        doppelganger dpg1 = new doppelganger(Path.of(String.valueOf(files[0])));
        doppelganger dpg2 = new doppelganger(Path.of(String.valueOf(files[0])));

        if ( dpg1.getGETObject().equals(dpg2.getGETObject()) ) System.out.println("Equals!");


         */

    }

    public static List<doppelganger> readfiles() throws Exception {
        List<doppelganger> gangers = new ArrayList<>();
        File directory = new File("/home/federico/Documenti/Thesis/Doppelganger/referenceDB/N-Linked");
        File[] files = directory.listFiles();

        int totalDoiless = 0;

        assert files != null;
        for (File file : files){
            Path doiJson = Path.of(String.valueOf(file));
            doppelganger doppel = new doppelganger(doiJson);
            gangers.add(doppel);

            totalDoiless += doppel.doiless;
            //System.out.println(doppel.getDoi()+"\n___________________________________________________________________");
        }
        return gangers;
    }
}
