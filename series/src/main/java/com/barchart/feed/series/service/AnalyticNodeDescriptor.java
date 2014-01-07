package com.barchart.feed.series.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.analytics.AnalyticDescriptor;
import com.barchart.feed.api.series.service.NodeType;
import com.barchart.feed.series.service.BarchartSeriesProvider.SearchDescriptor;


/**
 * Represents a {@link Node}'s meta information or schema. This class
 * fully contains information about its relationships to preceding 
 * {@link Node}s and their underlying processing units called {@link Analytic}s.
 * Additionally these descriptors are capable of and contain all the information
 * necessary to instantiate an instance of the {@link Analytic} without having 
 * knowledge or concern for the specific type being instantiated.
 * 
 * @author metaware
 * @see {@link Analytic}
 * @see {@link AnalyticDescriptor}
 */
public class AnalyticNodeDescriptor implements AnalyticDescriptor {
    private String networkName;
    private String nodeName;
    private Class<? extends Analytic> analyticClass;
    private int[] constructorArgs = new int[0];
    private String outputKey;
    private String[] timeFrames;
    private Map<String,AnalyticNodeDescriptor> inputDescriptors;
    private Map<String,String[]> inputTimeFrames;
    
    
    /**
     * Constructs a new AnalyticNodeDescriptor
     * 
     * @param networkName   the name mapping all the nodes belonging to this 
     *                      descriptor's network.
     * @param nodeName      the name of this particular node.
     */
    public AnalyticNodeDescriptor(String networkName, String nodeName) {
        this.networkName = networkName;
        this.nodeName = nodeName;
        inputDescriptors = new HashMap<String, AnalyticNodeDescriptor>();
        inputTimeFrames = new HashMap<String, String[]>();
    }
    
    /**
     * Returns the name of this descriptor.
     * @return  the name of this descriptor.
     */
    @Override
    public String getSpecifier() {
        return nodeName;
    }
    
    /**
     * Returns the {@link NodeType} this descriptor specifies.
     * @return  the {@link NodeType}
     */
    @Override
    public NodeType getType() {
        return NodeType.ANALYTIC;
    }
    
    /**
     * Returns the network name of this descriptor.
     * @return  the network name of this descriptor.
     */
    public String getNetworkName() {
        return networkName;
    }
    
    /**
     * Returns the String array containing the time frame
     * names.
     * 
     * @return  the String array containing the time frame
     *          names.
     */
    public String[] getTimeFrames() {
        return timeFrames;
    }
    
    /**
     * Sets the String array containing the time frame
     * names.
     * 
     * @param timeFrames    the String array containing the time frame
     *                      names.
     */
    public void setTimeFrames(String[] timeFrames) {
        this.timeFrames = timeFrames;
    }
    
    /**
     * Returns the {@link Analytic}'s class.
     * @return  the analytic's class.
     */
    @Override
    public Class<? extends Analytic> getAnalyticClass() {
        return analyticClass;
    }
    
    /**
     * Sets the {@link Analytic}'s class.
     * @param clazz the {@link Analytic}'s class.
     */
    @Override
    public void setAnalyticClass(Class<? extends Analytic> clazz) {
        this.analyticClass = clazz;
    }
    
    /**
     * Returns the underlying {@link Analytic}'s constructor
     * arguments.
     * @return  the underlying {@link Analytic}'s constructor arguments.
     */
    @Override
    public int[] getConstructorArgs() {
        return constructorArgs;
    }
    
   /**
    * Sets the underlying {@link Analytic}'s constructor
    * arguments.
    * @param    args    the underlying {@link Analytic}'s constructor arguments.
    */
    @Override
    public void setConstructorArgs(int[] args) {
        this.constructorArgs = args;
    }
    
    /**
     * Returns the output key used by the underlying {@link Analytic}
     * @return  the output key
     */
    public String getOutputKey() {
        return outputKey;
    }
    
    /**
     * Sets the output key used by the underlying {@link Analytic}
     * @param  key     the output key
     */
    public void setOutputKey(String key) {
        this.outputKey = key;
    }
    
    /**
     * Maps the specified key to the {@code AnalyticNodeDescriptor}
     * specified.
     * 
     * @param key       the key
     * @param desc      the node descriptor
     */
    public void mapInputDescriptor(String key, AnalyticNodeDescriptor desc) {
        this.inputDescriptors.put(key, desc);
    }
    
    /**
     * Maps the specified key to the time frame identifier specified.
     * @param key               the key
     * @param timeFrames        the array of time frame identifiers
     */
    public void mapTimeFrame(String key, String[] timeFrames) {
        this.inputTimeFrames.put(key, timeFrames);
    }
    
    /**
     * Returns the Analytic class sub type specified by this descriptor's
     * configuration.
     * 
     * @return  the Analytic class sub type
     * @throws IllegalStateException  if a suitable {@link Constructor} cannot be found.
     */
    public Analytic instantiateAnalytic() {
        Constructor<?>[] constructorArray = analyticClass.getConstructors();
        for (Constructor<?> constructor : constructorArray) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length == constructorArgs.length) {
                Object[] parameters = new Object[parameterTypes.length];
                for (int i = 0;i < parameterTypes.length;i++) {
                    parameters[i] = new Integer(this.constructorArgs[i]);
                }
                try {
                    return (Analytic)constructor.newInstance(parameters);
                }catch(Exception e) { }
            }
        }
        throw new IllegalStateException("Unable to instantiate Analytic class using constructor containing " + 
            (constructorArgs == null ? 0:constructorArgs.length) + " arguments");
    }
    
    /**
     * Returns a {@link List} of all input {@code AnalyticNodeDescriptor}s
     * 
     * @return	a {@link List} of all input {@code AnalyticNodeDescriptor}s
     */
    public List<AnalyticNodeDescriptor> getInputNodeDescriptors() {
    	return new ArrayList<AnalyticNodeDescriptor>(inputDescriptors.values());
    }
    
    /**
     * Returns the {@code AnalyticNodeDescriptor} which corresponds to the 
     * specified input key.
     * 
     * @param sourceKey     the key the descriptor is mapped to.
     * @return              the mapped node descriptor.
     */
    public AnalyticNodeDescriptor getInputNodeDescriptor(String sourceKey) {
        return this.inputDescriptors.get(sourceKey);
    }
    
    /**
     * Returns the names acting as place-holder-keys which identify which
     * time frame data is input to which outputs of the preceding node.
     *  
     * @param sourceKey     the key the {@link TimeFrame} is mapped to.
     * @return              the time frame's key.
     */
    public String[] getInputTimeframes(String sourceKey) {
        return this.inputTimeFrames.get(sourceKey);
    }
    
    /**
     * Returns the index of the {@link TimeFrame} identified by "key"
     * in the order it is declared.
     * 
     * @param tf      the identifier which corresponds with the {@link TimeFrame}
     *                for which the index is being returned. 
     * @return
     */
    public int getTimeframeIndex(String tf) {
        for(int i = 0;i < timeFrames.length;i++) {
            if(timeFrames[i].equals(tf)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Loads the specified {@link SearchDescriptor} with input definitions obtained
     * from this {@code AnalyticNodeDescriptor}, and the specified {@link SeriesSubscription}.
     * 
     * @param desc              the {@link SearchDescriptor} being initialized.
     * @param subscription      the {@link SeriesSubscription} describing the node indexed by
     *                          the specified SearchDescriptor.
     */
    public void loadSearchDescriptor(SearchDescriptor desc, SeriesSubscription subscription) {
        for (String key : this.inputDescriptors.keySet()) {
            AnalyticNodeDescriptor inputDesc = this.inputDescriptors.get(key);
            if (inputDesc != null) {
                SeriesSubscription priorSubscription = new SeriesSubscription();
                priorSubscription.loadFromNodeDescriptor(key, subscription, this);
                desc.addInputKeyMapping(key, priorSubscription);
            }
        }
    }
    
    /**
     * Returns a mapping of Strings which are keys mapped to {@link SeriesSubscription}s 
     * created within this method to identify the configured i/o {@link Period} data
     * that each {@link TimeFrame} will resolve to given the place holder key that data
     * is assigned to and the order of declaration within the underlying schema definition.
     * 
     * Each mapping returned is a statement of required input which will be represented by
     * a node found within a given existing network, or a newly created node which will supply
     * the required data. In this way, (by subsequently querying each ancestor for its required
     * input), an entire network can be newly built or partially built with some nodes arriving 
     * from components derived from pre-existing network nodes.
     * 
     * @param subscription  a {@link Subscription} defining the type and "@link TimeFrame} of data required.
     * @return  a mapping of keys to {@link SeriesSubscription}s which describe the data individuated into
     *          its required time frames.
     */
    public Map<String,SeriesSubscription> getRequiredSubscriptions(SeriesSubscription subscription) {
        Map<String, SeriesSubscription> req = new HashMap<String, SeriesSubscription>();

        for (String key: this.inputDescriptors.keySet()) {
            SeriesSubscription sub = new SeriesSubscription();
            sub.loadFromNodeDescriptor(key, subscription, this);
            req.put(key, sub);
        }
        
        return req;
    }

}
