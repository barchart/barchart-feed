package com.barchart.feed.api.series.network;

import java.util.List;

/**
 * Implementors of this interface specialize in operations on the 
 * schema which describes analytic networks as a whole. This includes
 * loading of networks and the resources which describe and locate them.
 * 
 * @author metaware
 */
public interface NetworkDescriptor extends NodeDescriptor {

    /**
     * Returns a list of {@link NodeDescriptor}s representing
     * nodes in the network this {@code NetworkDescriptor} describes.
     * 
     * @return      a list of {@link NodeDescriptor}s in this network
     */
    public <T extends NodeDescriptor> List<T> getNodeDescriptors();

    /**
     * Sets the list of {@link NodeDescriptor}s representing
     * nodes in the network this {@code NetworkDescriptor} describes.
     * 
     * @param         a list of {@link NodeDescriptor}s in this network
     */
    public <T extends NodeDescriptor> void setNodeDescriptors(List<T> nodes);

    /**
     * Returns a list of {@link AnalyticNodeDescriptor}s which are not themselves
     * listed as input for subsequent nodes *<em>WITHIN THEIR NETWORK</em>*, 
     * (i.e. they are at the bottom of their respective network), and therefore 
     * represent the major nodes one would subscribe to in order to fully 
     * instantiate this entire network, and receive all output data available.
     * 
     * @return	a list of nodes required to instantiate the whole network if subscribed to.
     * @throws	IllegalStateException	if this network has no configured or loaded nodes.
     */
    public <T extends NodeDescriptor> List<T> getPublishers();

}