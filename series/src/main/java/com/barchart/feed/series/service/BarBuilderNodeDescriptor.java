package com.barchart.feed.series.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.analytics.BarBuilderDescriptor;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.NodeType;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.series.analytics.BarBuilder;

public class BarBuilderNodeDescriptor implements BarBuilderDescriptor {
    private static final String BASE_STEP_FILE = "/baseSteps.txt";
    
    private static List<PeriodType> baseTypeSteps;
    static {
    	loadFromFile(BASE_STEP_FILE);
    }
    
    /** The class of type {@link Analytic} to instantiate */
    private Class<? extends Analytic> analyticsClass;
    /** The constructor argument */
    private Subscription constructorArg;
    /** Constructor ags for conventional {@link Analytic} style instantiation. */
    private int[] constructorArgs;
    
    /** The output key */
    private String outputKey;
    
    
    /**
     * Constructs a new {@code BarBuilderNodeDescriptor}
     */
    public BarBuilderNodeDescriptor() {}
    
    private static void loadFromFile(String path) {
        loadFromStream(BarBuilderNodeDescriptor.class.getResourceAsStream(path));
    }
    
    private static void loadFromStream(InputStream stream) {
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            if(baseTypeSteps == null) {
                baseTypeSteps = Collections.synchronizedList(new ArrayList<PeriodType>());
            }else{
                baseTypeSteps.clear();
            }
            
            while((line = buff.readLine()) != null) {
                baseTypeSteps.add(PeriodType.forString(line.trim()));
            }
           
        }catch(Exception e) {e.printStackTrace();}
    }
    
    public void reloadFile() {
        loadFromFile(BASE_STEP_FILE);
    }
    
    public void reloadStream(InputStream stream) {
        loadFromStream(stream);
    }
    
    /**
     * Returns the type which is a base type as configured by the application
     * which also has a {@link PeriodType} lower than the type specified or the
     * lowest PeriodType configured as a base type.
     * 
     * @param type
     * @return
     */
    static PeriodType getLowerBaseType(PeriodType type) {
        PeriodType retType = null;
        int idx = 0;
        while(idx < baseTypeSteps.size() && (retType == null || baseTypeSteps.get(idx).isLowerThan(type))) {
            retType = baseTypeSteps.get(idx++);
        }
        return retType;
    }
    
    /**
     * Returns a {@link Subscription} containing a  {@link TimeFrame} whose {@link Period}
     * is of a lower type <em>AND</em> is pre-configured to be an acceptable bar building
     * {@link PeriodType} suitable for bar building.
     * 
     * @param input     the Subscription whose Period should be lowered
     * @return          a new {@link SeriesSubscription} containing the pre-approved 
     *                  lowered {@link Period} type.
     */
    public static SeriesSubscription getLowerSubscription(SeriesSubscription input) {
        Period next = null;
        if(input.getTimeFrames()[0].getPeriod().size() > 1) {
            next = new Period(input.getTimeFrames()[0].getPeriod().getPeriodType(), 1);
        }else{
            next = new Period(getLowerBaseType(input.getTimeFrames()[0].getPeriod().getPeriodType()), 1);
        }
        
        SeriesSubscription sSub = new SeriesSubscription(input);
        sSub.setTimeFrames(new TimeFrame[] {
            new TimeFrame(next, input.getTimeFrames()[0].getStartDate(), input.getTimeFrames()[0].getEndDate()) });
        return sSub;
    }
    
    /**
     * Adds a new {@link BarBuilderOld} to the specified List, using the input {@link Subscription}
     * and new {@link Period}
     * 
     * @param chain
     * @param input
     * @param next
     * @return
     */
    private SeriesSubscription addNextNodeToChain(List<AnalyticNode> chain, SeriesSubscription input, Period next) {
        SeriesSubscription sSub = new SeriesSubscription(input);
        sSub.setTimeFrames(new TimeFrame[] {
            new TimeFrame(next, input.getTimeFrames()[0].getStartDate(), input.getTimeFrames()[0].getEndDate()) });
        setConstructorArg(sSub);
        AnalyticNode bb = new AnalyticNode(instantiateBuilderAnalytic());
        bb.addOutputKeyMapping(BarBuilder.OUTPUT_KEY, sSub);
        chain.get(chain.size() - 1).addInputKeyMapping(BarBuilder.INPUT_KEY, sSub);
        chain.add(bb);
        return sSub;
    }
    
    /**
     * Returns an ordered {@link List} of {@link AnalyticContainer} nodes, which are ordered according
     * to their output {@link Subscription} {@link Period}s. The nodes themselves are not connected
     * to each other, but are understood to represent the steps needed to proceed from the specified
     * "derivableSubscription" to the parameter "subscriptionTarget".
     * 
     * The returned List is ordered in such a way that the zero'th element can be connected directly to the
     * {@link Node} whose output is defined by the specified derivableSubscription parameter. The rest of
     * the Nodes can then be hooked up to each other such that a path or chain is created through the
     * Node in the list whose output is defined by the Subscription "subscriptionTarget".
     * 
     * @param   derivableSubscription   the Subscription needed as input by the zero'th element in the returned List.
     * @param   subscriptionTarget      the output Subscription of the last element in the returned List.
     * @return  the list of nodes proceeding from the {@link Node} which will connect to the Node whose subscription is 
                the first parameter, to the Node whose output {@link Subscription} is the second.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<AnalyticNode> getNodeChain(Subscription derivableSubscription, Subscription subscriptionTarget) {
        if(derivableSubscription == null || subscriptionTarget == null || derivableSubscription.equals(subscriptionTarget)) {
            throw new IllegalArgumentException("Source and target cannot be null or equal to each other.");
        }
        
        SeriesSubscription higher = (SeriesSubscription)subscriptionTarget;
        SeriesSubscription lower = (SeriesSubscription)derivableSubscription;
        
        List<AnalyticNode> retVal = new ArrayList<AnalyticNode>();
        setAnalyticClass(BarBuilder.class);
        setConstructorArg(higher);
        AnalyticNode bb = new AnalyticNode(instantiateBuilderAnalytic());
        bb.addOutputKeyMapping(BarBuilder.OUTPUT_KEY, higher);
        retVal.add(bb);
        
        //First reduce the interval and add a node for that.
        if(higher.getTimeFrames()[0].getPeriod().size() > 1) {
            Period smaller = new Period(higher.getTimeFrames()[0].getPeriod().getPeriodType(), 1);
            if(!smaller.equals(lower.getTimeFrames()[0].getPeriod())) {
                higher = addNextNodeToChain(retVal, higher, smaller);
            }else{
                higher = lower;
            }
        }
        
        //Next add processors to fill base type gap between "derivable" and target.
        PeriodType smallerType = higher.getTimeFrames()[0].getPeriod().getPeriodType();
        while(higher.getTimeFrames()[0].getPeriod().getPeriodType() !=  lower.getTimeFrames()[0].getPeriod().getPeriodType() && 
            (smallerType = getLowerBaseType(smallerType)) != lower.getTimeFrames()[0].getPeriod().getPeriodType()) {
            Period smaller = new Period(smallerType, 1);
            higher = addNextNodeToChain(retVal, higher, smaller);
        }
        
        //Reverse so that processors ascend in period type
        Collections.reverse(retVal);
        
        return retVal;
    }
    
    public String toString() {
        return "BarBuilderNodeDescriptor";
    }

    @Override
    public Class<? extends Analytic> getAnalyticClass() {
        return analyticsClass;
    }

    @Override
    public void setAnalyticClass(Class<? extends Analytic> clazz) {
        this.analyticsClass = clazz;
    }

    @Override
    public int[] getConstructorArgs() {
        return constructorArgs;
    }

    /**
     * Intentionally not implemented.
     */
    @Override
    public void setConstructorArgs(int[] args) {
        this.constructorArgs = args;
    }
    
    /**
     * Returns the output key used by the underlying {@link Analytic}
     * @return  the output key
     */
    @Override
    public String getOutputKey() {
        return outputKey;
    }
    
    /**
     * Sets the output key used by the underlying {@link Analytic}
     * @param  key     the output key
     */
    @Override
    public void setOutputKey(String key) {
        this.outputKey = key;
    }

    
    @Override
    public Analytic instantiateAnalytic() {
        Constructor<?>[] constructorArray = analyticsClass.getConstructors();
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
     * Instantiates the sub type of the {@link Analytic} class specified.
     * 
     * @param s     a derivative of {@link Subscription}
     * @return      the instantiated Analytic
     */
    @Override
    public Analytic instantiateBuilderAnalytic() {
        Constructor<?>[] constructorArray = analyticsClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructorArray) {
            constructor.setAccessible(true);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length == 1 && (getConstructorArg().getClass().equals(parameterTypes[0]))) {
                try {
                    return (Analytic)constructor.newInstance(new Object[] { getConstructorArg() });
                }catch(Exception e) { e.printStackTrace(); }
            }
        }
        throw new IllegalStateException("Unable to instantiate Analytic class using constructor containing " + 
             "1 arguments of type <S extends Subscription> using argument: [s=" + getConstructorArg().getClass().getName() + "]");
    }

    @Override
    public String getSpecifier() {
        return NodeType.IO.toString();
    }

    @Override
    public NodeType getType() {
        return NodeType.IO;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Subscription> T getConstructorArg() {
        return (T)constructorArg;
    }

    @Override
    public <T extends Subscription> void setConstructorArg(T arg) {
        this.constructorArg = arg;
    }

}
