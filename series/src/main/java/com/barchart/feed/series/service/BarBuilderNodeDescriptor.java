package com.barchart.feed.series.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.series.service.AnalyticContainer;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.api.series.temporal.TimeFrame;

public class BarBuilderNodeDescriptor extends NodeDescriptor {
    private static final String BASE_STEP_FILE = "/baseSteps.txt";
    
    private static List<PeriodType> baseTypeSteps;
    static {
    	loadFromFile(BASE_STEP_FILE);
    }
    
    public BarBuilderNodeDescriptor() {
        super(NodeDescriptor.TYPE_IO);
    }
    
    public BarBuilderNodeDescriptor(String specifier) {
        super(specifier);
    }
    
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
     * Adds a new {@link BarBuilderOld} to the specified List, using the input {@link Subscription}
     * and new {@link Period}
     * 
     * @param chain
     * @param input
     * @param next
     * @return
     */
    private SeriesSubscription addNextNodeToChain(List<AnalyticContainer> chain, SeriesSubscription input, Period next) {
        SeriesSubscription sSub = new SeriesSubscription(input);
        sSub.setTimeFrames(new TimeFrame[] {
            new TimeFrame(next, input.getTimeFrames()[0].getStartDate(), input.getTimeFrames()[0].getEndDate()) });
        BarBuilderOld bb = new BarBuilderOld(sSub);
        chain.get(chain.size() - 1).addInputSubscription(null, bb.getOutputSubscription(null));
        chain.add(bb);
        return sSub;
    }
    
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
    @Override
    public List<AnalyticContainer> getProcessorChain(Subscription derivableSubscription, Subscription subscriptionTarget) {
        if(derivableSubscription == null || subscriptionTarget == null || derivableSubscription.equals(subscriptionTarget)) {
            throw new IllegalArgumentException("Source and target cannot be null or equal to each other.");
        }
        
        SeriesSubscription higher = (SeriesSubscription)subscriptionTarget;
        SeriesSubscription lower = (SeriesSubscription)derivableSubscription;
        
        List<AnalyticContainer> retVal = new ArrayList<AnalyticContainer>();
        BarBuilderOld bb = new BarBuilderOld(higher);
        retVal.add(bb);
        
        //First reduce the interval and add a node for that.
        if(higher.getTimeFrames()[0].getPeriod().size() > 1) {
            Period smaller = new Period(higher.getTimeFrames()[0].getPeriod().getPeriodType(), 1);
            higher = addNextNodeToChain(retVal, higher, smaller);
        }
        
        //Next add processors to fill base type gap between "derivable" and target.
        PeriodType smallerType = higher.getTimeFrames()[0].getPeriod().getPeriodType();
        while((smallerType = getLowerBaseType(smallerType)) != lower.getTimeFrames()[0].getPeriod().getPeriodType()) {
            Period smaller = new Period(smallerType, 1);
            higher = addNextNodeToChain(retVal, higher, smaller);
        }
        
        //Reverse so that processors ascend in period type
        Collections.reverse(retVal);
        
        return retVal;
    }
    
    public String toString() {
        return getSpecifier();
    }

}
