package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;
import org.expasy.glyconnect.doppelganger.doppelganger.reader;

import java.util.List;
//GitHub access token (expires 15 june): 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

public class scorer {
    public static void main(String[] args) throws Exception {
        List<doppelganger> gangers = reader.readfiles("proteinsAll");

        for (int f = 0; f < gangers.size(); f++) {
            linkSim linkSim = new linkSim(gangers.get(f));

            if (linkSim.getLinkStringVT().length() > 0) {
                System.out.println("\n" + linkSim.getIdentifier() +
                        "\nStringVT: " + linkSim.getLinkStringVT() +
                        "\nCountsVT: " + linkSim.getLinkCountVT() +
                        "\nFreqsVT:  " + linkSim.getLinkFreqVT() +
                        "\n\nStringVF: " + linkSim.getLinkStringVF() +
                        "\nFreqsVF:  " + linkSim.getLinkFreqVF() +
                        "\n_____________________________________________________________________________________________________________________________");
            }
        }
    }
}
