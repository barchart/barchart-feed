package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Analytic;
import com.barchart.feed.api.series.services.Assembler;
import com.barchart.feed.api.series.services.FeedMonitorService;
import com.barchart.feed.api.series.services.Node;
import com.barchart.feed.api.series.services.NodeDescriptor;
import com.barchart.feed.api.series.services.Processor;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.api.series.services.Subscription;

/**
 * Queryable framework for providing {@link TimeSeries} objects.
 * 
 * @author David Ray
 */
public class BarchartSeriesProvider {
	private FeedMonitorService feedService;
	
	/** Constains output-level/Subscribable IO nodes */
	private List<Node> ioNodes = Collections.synchronizedList(new ArrayList<Node>());
	/** Constains output-level/Subscribable {@link Analytic} nodes */
    private List<Node> analyticNodes = Collections.synchronizedList(new ArrayList<Node>());
    /** Constains output-level/Subscribable {@link Analytic} nodes */
    private List<Node> assemblers = Collections.synchronizedList(new ArrayList<Node>());
	
	
	
	/**
	 * Instantiates a new {@code BarchartSeriesProvider}
	 * @param feedService	an implementation of {@link FeedMonitorService}
	 */
	public BarchartSeriesProvider(FeedMonitorService feedService) {
		this.feedService = feedService;
	}
	
	/**
	 *
	 * @param query
	 * @return
	 */
	public <T extends TimePoint> Observable<TimeSeries<T>> fetch(final Query query) {
		Instrument inst = lookupInstrument(query.getSymbol());
		SeriesSubscription subscription = (SeriesSubscription)query.toSubscription(inst);
		
		System.out.println("inst = " + inst.symbol());
		
		Node node = lookupNode(subscription);
		
		return null;
	}
	
	private Instrument lookupInstrument(String symbol) {
		Instrument inst = feedService.lookupInstrument(symbol);
		return inst;
	}
	
	private Node lookupNode(Subscription subscription) {
	    boolean isIO = subscription.getNodeDescriptor().getSpecifier().equals(NodeDescriptor.TYPE_IO);
	    boolean isAssembler = subscription.getNodeDescriptor().getSpecifier().equals(NodeDescriptor.TYPE_ASSEMBLER);
	    
	    Node retVal = null;
	    List<Node> derivables = null;
	    if(!isAssembler) {
		    Node derivableLookup = null;
		    List<Node> searchList = isIO ? ioNodes : analyticNodes;
		    for(Node node : searchList) {
	            if(node.getOutputSubscriptions().contains(subscription)) {
	                retVal = node;
	            }else if((derivableLookup = node.lookup(subscription)[1]) != null) {
	                if(derivables == null) {
	                    derivables = new ArrayList<Node>();
	                }
	                derivables.add(derivableLookup);
	            }
	        }
	    }
	    
	    if(!isAssembler && retVal == null) {
	        if(derivables != null) {
	            Node derivableNode = getBestDerivableNode(derivables, subscription);//Need algorithm to determine best derivable node
	            Subscription derivableSubscription = derivableNode.getDerivableOutputSubscription(subscription);
                List<Processor> processorChain = subscription.getNodeDescriptor().getProcessorChain(derivableSubscription, subscription);
                for(int i = 0;i < processorChain.size();i++) {
                	Processor p = processorChain.get(i);
                    switch(p.getCategory()) {
                        case BAR_BUILDER: { 
                        	BarBuilder child = (BarBuilder)p;
                        	ioNodes.add(child);
                        	derivableNode.addChildNode(child);
                        	child.setInputTimeSeries(child.getInputSubscription(null), 
                        		derivableNode.getOutputTimeSeries(child.getInputSubscription(null)));
                        	derivableNode = child;
                        	
                        	break; 
                        }
                        case ANALYTIC: { break; }//Add the AnalyticNode later...
                    }
                }
            }
	        
	        retVal = isIO ? new BarBuilder(subscription) : null;
            List<Subscription> inputs = retVal.getInputSubscriptions();
            for(Subscription s : inputs) {
                Node n = lookupNode(s);
                n.addChildNode(retVal);
                if(isIO) {
                	ioNodes.add(retVal);
                	((BarBuilder)retVal).setInputTimeSeries(s, n.getOutputTimeSeries(s));
                }else{
                	analyticNodes.add(retVal);
                	((AnalyticNode)retVal).setInputTimeSeries(s, n.getOutputTimeSeries(s));
                }
            }
        }else if(isAssembler) {
        	Assembler assembler = new Distributor((SeriesSubscription)subscription);
        	assemblers.add((Node)assembler);
        	retVal = (Node)assembler;
        }
	    
	    return retVal;
	}
	
	private Node getBestDerivableNode(List<Node> derivableNodes, Subscription subscription) {
	    Node n = derivableNodes.get(0);//Optimize this later
	    return n;
	}
	
}
