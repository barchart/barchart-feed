package com.barchart.feed.api.series.analytics;

/**
 * Identifies the type of {@link Node} for use within the Barchart
 * Analytic Network.
 * 
 * @author David Ray
 */
public enum NodeType { 
    ASSEMBLER("RAW"),
    IO("IO"),
    ANALYTIC("ANL"),
    EXPRESSION("EXP"),
    NETWORK("NET"),
    SPREAD("SPD"),
    SYNTHETIC("SYN"),
    ADHOC("AHC");
    
    private String abbrev;
    
    private NodeType(String abbrev) {
        this.abbrev = abbrev;
    }
    
    /**
     * Returns a 3-letter code which corresponds to this
     * {@code NodeType}
     * 
     * @return	a 3-letter code which corresponds to this {@code NodeType}
     */
    public String code() {
        return this.abbrev;
    }
    
    /**
     * Returns the {@code NodeType} corresponding either to the enum's
     * name or the enum's abbreviation as returned from the {@link #code()}
     * method.
     * 
     * @param string	the identifier for the type to return.
     * @return			the corresponding NodeType
     */
    public static NodeType forString(String string) {
    	for(NodeType nt : values()) {
    		if(string.toUpperCase().equals(nt.toString()) || 
    			string.toUpperCase().equals(nt.abbrev)) {
    			return nt;
    		}
    	}
    	return null;
    }
    
}