package org.expasy.glyconnect.doppelganger.doppelganger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.glyconnect.doppelganger.doppelganger.GETObject.GETObject;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.POSTObject;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.link;
import org.expasy.glyconnect.doppelganger.doppelganger.POSTObject.node;
import org.expasy.glyconnect.doppelganger.scorer.compare;
import org.expasy.glyconnect.doppelganger.scorer.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// from this class, call all the others and make a single object.
public class doppelganger {
    private String identifier;
    private String glycanType;

    private final GETObject GETObject;
    private final POSTObject POSTObject;

    private ArrayList<node> realNodes = new ArrayList<>();
    private ArrayList<node> virtualNodes = new ArrayList<>();

    private ArrayList<link> realLinks;
    private ArrayList<link> virtualLinks;

    private double networkDensityVT;
    private double networkDensityVF;

    /* Attributes for node similarity */
    private HashMap<String, Integer> propertiesCountVT = new HashMap<>();
    private HashMap<String, Integer> propertiesCountVF = new HashMap<>();
    private HashMap<String, Double> propertiesFreqVT = new HashMap<>();
    private HashMap<String, Double> propertiesFreqVF = new HashMap<>();

    /* Attributes for link similarity */
    private String linkStringVT; // String of all the links in the doppelganger object, virtuals included.
    private String linkStringVF; // String of all the links in the doppelganger object, virtuals excluded.
    private HashMap<Character, Integer> linkCountVT = new HashMap<>(); // Counts the occurrences of each link in the network, virtuals included.
    private HashMap<Character, Integer> linkCountVF = new HashMap<>(); // Counts the occurrences of each link in the network, virtuals excluded.
    private HashMap<Character, Double> linkFreqVT = new HashMap<>(); // Link frequencies relative to the total number of links in the network, virtuals included;
    private HashMap<Character, Double> linkFreqVF = new HashMap<>(); // Link frequencies relative to the total number of links in the network, virtuals excluded;

    public int doiless;

    /**
     * Main constructor
     *
     * @param sourceJson Path of the json file to process. Depending on the file, the file name can be:
     *                   - the DOI under which the network is referenced in Glyconnect;
     *                   - the identifier composed as "GlyconnectId;UniprotAcc".
     */
    public doppelganger(Path sourceJson) throws Exception {
        /*
        if ( sourceJson.toString().contains("proteinsAll") || sourceJson.toString().contains("diseasesAll") )
            this.identifier = sourceJson.getFileName().toString(); //.replace("\n","");

        if ( sourceJson.toString().contains("referencesAll") )
        */
        this.identifier = sourceJson.getFileName().toString().replace("_","/");

        this.setGlycanType(sourceJson);

        String jsonContent = Files.readString((sourceJson));

        JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

        JsonArray GETSection = jsonObject.getAsJsonArray("GETRequest");
        JsonObject POSTSection = jsonObject.getAsJsonObject("POSTRequest");

        this.GETObject = new GETObject(GETSection, this.glycanType);
        this.doiless = this.GETObject.doiless;

        this.POSTObject = new POSTObject(POSTSection);
        
        /* Set node similarity attributes */
        this.setRealNodes();
        this.setVirtualNodes();

        this.setPropertiesCount(true); // Sets propertiesCountVT
        this.setPropertiesCount(false); // Sets propertiesCountVF

        this.propertiesFreqVT = helper.propertiesFrequencies(this.propertiesCountVT, this.nodesNumberVT());
        this.propertiesFreqVF = helper.propertiesFrequencies(this.propertiesCountVF, this.nodesNumberVF());
        
        /* Set link similarity attributes */
        this.setLinkArrays(); // Sets both realLinks and virtualLinks
        this.setLinkStrings(); // Sets both linkStringVT and linkStringVF
        this.linkCountVT = helper.countLinks(this.linkStringVT);
        this.linkCountVF = helper.countLinks(this.linkStringVF);

        this.linkFreqVT = helper.linkFrequencies(this.linkCountVT, this.linksNumberVT());
        this.linkFreqVF = helper.linkFrequencies(this.linkCountVF, this.linksNumberVF());

        /* Set others */
        this.setNetworkDensityVT();
        this.setNetworkDensityVF();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setGlycanType(Path doiJson) {
        String path = doiJson.toString();

        if ( path.contains("N-Linked") ) this.glycanType = "N-Linked";
        else if ( path.contains("O-Linked") ) this.glycanType = "O-Linked";
        else this.glycanType = "Unknown";
    }

    public double getNetworkDensityVT() {
        return this.networkDensityVT;
    }

    public void setNetworkDensityVT() {
        this.networkDensityVT = compare.networkDensity(this.nodesNumberVT(), this.linksNumberVT());
    }

    public double getNetworkDensityVF() {
        return this.networkDensityVF;
    }

    public void setNetworkDensityVF() {
        this.networkDensityVF = compare.networkDensity(this.realNodesNumber(), this.linksNumberVF());
    }

    public GETObject getGETObject() {
        return GETObject;
    }

    public POSTObject getPOSTObject() {
        return POSTObject;
    }

    public String getGlycanType() {
        return glycanType;
    }

    /* Functions for node similarity */
    public void setRealNodes() {
        /* Fills realNodes and virtualNodes from doppelganger.POSTObject.nodes */
        Map<Integer, node> nodes = this.getPOSTObject().getNodes();

        ArrayList<node> real = new ArrayList<>();

        for (Integer i : nodes.keySet()) {
            if ( (!nodes.get(i).isVirtual()) && !(real.contains(nodes.get(i))) )
                real.add(nodes.get(i));
        }
        if ( real.size() != 0 )
            this.realNodes = real;
    }

    public void setVirtualNodes() {
        Map<Integer, node> nodes = this.getPOSTObject().getNodes();

        ArrayList<node> virtual = new ArrayList<>();

        for (Integer i : nodes.keySet()) {
            if ( nodes.get(i).isVirtual() && !(virtual.contains(nodes.get(i))) ) {
                virtual.add(nodes.get(i));
            }
        }

        if ( virtual.size() != 0 )
            this.virtualNodes = virtual;
    }

    public void setPropertiesCount(boolean includeVirtual) {
        HashMap<String, Integer> propertiesCount = new HashMap<>();

        ArrayList<node> nodes = new ArrayList<>(this.realNodes);

        if ( includeVirtual ) nodes.addAll(this.virtualNodes);

        propertiesCount.putIfAbsent("Neutral", 0);
        propertiesCount.putIfAbsent("Fucosylated", 0);
        propertiesCount.putIfAbsent("Sialylated", 0);
        propertiesCount.putIfAbsent("Fuco-sialylated", 0);
        propertiesCount.putIfAbsent("Oligomannose", 0);
        propertiesCount.putIfAbsent("Sulfated", 0);


        for (node node : nodes) {
            for (String prop : node.getProperties()){
                propertiesCount.replace(prop, propertiesCount.get(prop)+1);
                /*
                Each time a property is present in a node,
                said property is get from propertiesCount
                and its value is increased by 1
                */
            }
        }

        if ( includeVirtual ) {
            this.propertiesCountVT = propertiesCount;
        }
        else {
            this.propertiesCountVF = propertiesCount;
        }
    }
    public ArrayList<node> getRealNodes() {
        return this.realNodes;
    }

    public ArrayList<node> getVirtualNodes() {
        return this.virtualNodes;
    }

    public HashMap<String, Integer> getPropertiesCountVT() {
        return this.propertiesCountVT;
    }

    public HashMap<String, Integer> getPropertiesCountVF() {
        return this.propertiesCountVF;
    }

    public HashMap<String, Double> getPropertiesFreqVT() {
        return this.propertiesFreqVT;
    }
    public HashMap<String, Double> getPropertiesFreqVF() {
        return this.propertiesFreqVF;
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

    public int nodesNumberVF() {
        return this.realNodesNumber();
    }
    
    /* Functions for link similarity */
    public void setLinkStrings() {
        /* Composes both linkStringVT and linkStringVF */
        Map<Integer, link> links = this.getPOSTObject().getLinks();
        StringBuilder linksVT = new StringBuilder();
        StringBuilder linksVF = new StringBuilder();
        linksVT.append("");
        linksVF.append("");

        ArrayList<String> virtuals = new ArrayList<>();
        for (node virtual : this.virtualNodes) virtuals.add(virtual.toString());

        for (Integer i : links.keySet()) {
            linksVT.append(links.get(i).getCondensedFormat()); //VT takes both virtual and real links

            if ( !(virtuals.contains(links.get(i).getSource()))
                    && !(virtuals.contains(links.get(i).getTarget())) )
                linksVF.append(links.get(i).getCondensedFormat()); //VF takes only links between two real nodes
        }

        this.linkStringVT = linksVT.toString();
        this.linkStringVF = linksVF.toString();
    }

    public void setLinkArrays() {
        /* Composes both virtualLinks and realLinks arraylists */
        Map<Integer, link> links = this.getPOSTObject().getLinks();
        ArrayList<link> realLinks = new ArrayList<>();
        ArrayList<link> virtualLinks = new ArrayList<>();

        ArrayList<String> virtuals = new ArrayList<>();
        for (node virtual : this.virtualNodes) virtuals.add(virtual.toString());

        for (Integer i : links.keySet()) {

            if ( !(virtuals.contains(links.get(i).getSource()))
                && !(virtuals.contains(links.get(i).getTarget()))
                && !(realLinks.contains(links.get(i))) ) {

                realLinks.add(links.get(i)); // realLinks takes only links between two real nodes
            }
            else {
                if ( !(virtualLinks.contains(links.get(i))) ) virtualLinks.add(links.get(i));
            }
        }

        this.realLinks = realLinks;
        this.virtualLinks = virtualLinks;
    }
    public String getLinkStringVT() {
        return this.linkStringVT;
    }

    public String getLinkStringVF() {
        return this.linkStringVF;
    }

    public ArrayList<link> getRealLinks() {
        return realLinks;
    }

    public ArrayList<link> getVirtualLinks() {
        return virtualLinks;
    }

    public HashMap<Character, Integer> getLinkCountVT() {
        return this.linkCountVT;
    }

    public HashMap<Character, Integer> getLinkCountVF() {
        return this.linkCountVF;
    }

    public int virtualLinksNumber() {
        return this.linksNumberVT()-this.linksNumberVF();
    }

    public int linksNumberVT() {
        if ( this.linkStringVT == null ) return 0;
        return this.linkStringVT.length();
    }

    public int linksNumberVF() {
        if ( this.linkStringVF == null ) return 0;
        return this.linkStringVF.length();
    }

    public HashMap<Character, Double> getLinkFreqVT() {
        return this.linkFreqVT;
    }

    public HashMap<Character, Double> getLinkFreqVF() {
        return this.linkFreqVF;
    }

    /* Misc */
    public boolean equals(doppelganger that) {
        if ( this.identifier != null && that.identifier != null && this.identifier.equals(that.identifier) ) {
            if ( this.glycanType.equals(that.glycanType) ) {
                if ( this.getGETObject().equals(that.getGETObject()) ) {
                    if ( this.getPOSTObject().equals(that.getPOSTObject()) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInside(List<doppelganger> list) {
        for (doppelganger doppel : list) {
            if ( this.equals(doppel) ) return true;
        }
        return false;
    }

    public void attributesChecker() {
        System.out.println(this.getIdentifier());

        System.out.println("Real nodes: "+ this.getRealNodes() +
                "\nVirtual nodes: "+ this.getVirtualNodes() +
                "\nReal nodes number: "+ this.realNodesNumber() +
                "\nVirtual nodes number: "+this.virtualNodesNumber() +
                "\nReal links: "+ this.getRealLinks() +
                "\nVirtual links: "+ this.getVirtualLinks() +
                "\nReal links string: "+this.getLinkStringVT() +
                "\nVirtual links string: "+this.getLinkStringVF() +
                "\nReal links number: "+this.linksNumberVF() +
                "\nVirtual links number: "+this.virtualLinksNumber() +
                "\nNetwork density VT: "+this.getNetworkDensityVT() +
                "\nNetwork density VF: "+this.getNetworkDensityVF()+
                "\n\nProperties count VT: "+this.getPropertiesCountVT()+
                "\nProperties count VF: "+this.getPropertiesCountVF()+
                "\nProperties frequencies VT: "+this.getPropertiesFreqVT() +
                "\nProperties frequencies VF: "+this.getPropertiesFreqVF() +
                "\n\nLink count VT: "+this.getLinkCountVT() +
                "\nLink count VF: "+this.getLinkCountVF() +
                "\nLink frequencies VT: "+this.getLinkFreqVT() +
                "\nLink frequencies VF: "+this.getLinkFreqVF() +
                "\n_________________________________________________________________________________________________________________________________");
    }
}
