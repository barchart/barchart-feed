package com.barchart.feed.api.series.service;

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
    
    public String code() {
        return this.abbrev;
    }
    
}