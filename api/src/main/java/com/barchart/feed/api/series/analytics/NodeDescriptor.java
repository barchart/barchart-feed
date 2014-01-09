package com.barchart.feed.api.series.analytics;


public interface NodeDescriptor {
    /**
     * Returns the specifier, name or id of this descriptor's node.
     * @return  the specifier String
     */
    public String getSpecifier();
    
    /**
     * Returns the {@link NodeType} this descriptor specifies.
     * @return  the {@link NodeType}
     */
    public NodeType getType();
    
}
