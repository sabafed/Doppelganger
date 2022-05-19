package org.expasy.glyconnect.doppelganger.scorer;

import org.expasy.glyconnect.doppelganger.doppelganger.doppelganger;

import java.util.ArrayList;

public class nodeSim {
    private String identifier;
    private final doppelganger doppelganger;

    private ArrayList<String> realNodes = new ArrayList<String>();
    private ArrayList<String> virtualNodes = new ArrayList<String>();

    public nodeSim(doppelganger doppelganger) {
        this.identifier = doppelganger.getIdentifier();
        this.doppelganger = doppelganger;

        this.setNodesArrays(); // Sets both realNodes and virtualNodes arrays


    }
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ArrayList<String> getRealNodes() {
        return realNodes;
    }

    public ArrayList<String> getVirtualNodes() {
        return virtualNodes;
    }

    /* Fills realNodes and virtualNodes from doppelganger.POSTObject.nodes */
    public void setNodesArrays() {
        if ( this.doppelganger.getRealNodes().size() != 0 )
            this.realNodes = this.doppelganger.getRealNodes();

        if ( this.doppelganger.getVirtualNodes().size() != 0 )
            this.virtualNodes = this.doppelganger.getRealNodes();
    }

    public int realNodesNumber() {
        return this.realNodes.size();
    }

    public int virtualNodesNumber() {
        return this.virtualNodes.size();
    }

    public int nodesNumberVT() {
        return this.realNodesNumber()+this.virtualNodesNumber();
    }

    @Override
    public String toString() {
        return this.identifier;
    }
}
