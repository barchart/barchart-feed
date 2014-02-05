package com.barchart.feed.api.series.network;


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
    /**
     * Returns the network name of this descriptor.
     * @return  the network name of this descriptor.
     */
    public String getNetworkName();
    /**
     * Sets the network name of this descriptor.
     * @param  the network name of this descriptor.
     */
    public void setNetworkName(String name);

    
    
}
