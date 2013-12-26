package com.barchart.feed.series.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.TimeSeriesObservable;
import com.barchart.feed.api.series.service.Analytic;
import com.barchart.feed.api.series.service.FeedMonitorService;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.Processor;
import com.barchart.feed.api.series.service.Query;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.series.DataBar;
import com.barchart.feed.series.DataSeries;

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
	/** Subscribers for a particular {@link Subscription} */
    private Map<Subscription, Observer<Span>> subscribers = new HashMap<Subscription, Observer<Span>>();
    private Map<Subscription, List<Distributor>> subscriberAssemblers = new HashMap<Subscription, List<Distributor>>();
	
	
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
	public <T extends TimePoint> TimeSeriesObservable fetch(final Query query) {
		Instrument inst = feedService.lookupInstrument(query.getSymbol());
		SeriesSubscription subscription = (SeriesSubscription)query.toSubscription(inst);
		System.out.println("inst = " + inst.symbol());
		Node node = lookupNode(subscription, subscription);
		TimeSeries<T> series = node.getOutputTimeSeries(subscription);
		
		return (new TimeSeriesObservable(new SeriesSubscriber(subscription, node), series) {
		    @SuppressWarnings("unchecked")
            public TimeSeries<?> getTimeSeries() { return (TimeSeries<TimePoint>)this.series; }
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Node lookupNode(Subscription subscription, Subscription original) {
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
                        	BarBuilderOld<DataBar> child = (BarBuilderOld<DataBar>)p;
                        	ioNodes.add(child);
                        	derivableNode.addChildNode(child);
                        	child.addParentNode(derivableNode);
                        	child.setInputTimeSeries(child.getInputSubscription(null), 
                        		(DataSeries)derivableNode.getOutputTimeSeries(child.getInputSubscription(null)));
                        	derivableNode = child;
                        	
                        	break; 
                        }
                        case ANALYTIC: { break; }//Add the AnalyticNode later...
                        default:
                    }
                }
            }
	        
	        retVal = isIO ? new BarBuilderOld<DataBar>(subscription) : null;
            List<Subscription> inputs = retVal.getInputSubscriptions();
            for(Subscription s : inputs) {
                Node n = lookupNode(s, original);
                n.addChildNode(retVal);
                retVal.addParentNode(n);
                if(isIO) {
                	ioNodes.add(retVal);
                	((BarBuilderOld)retVal).setInputTimeSeries(s, n.getOutputTimeSeries(s));
                }else{
                	analyticNodes.add(retVal);
                	((AnalyticNode)retVal).setInputTimeSeries(s, n.getOutputTimeSeries(s));
                }
            }
        }else if(isAssembler) {
            Distributor assembler = new Distributor((SeriesSubscription)subscription);
        	assemblers.add(assembler);
        	
        	List<Distributor> dists = null;
        	if((dists = subscriberAssemblers.get(original)) == null) {
        	    subscriberAssemblers.put(original, dists = new ArrayList<Distributor>());
        	}
        	dists.add(assembler);
        	
        	retVal = assembler;
        }
	    
	    return retVal;
	}
	
	private Node getBestDerivableNode(List<Node> derivableNodes, Subscription subscription) {
	    Node n = derivableNodes.get(0);//Optimize this later
	    return n;
	}
	
	public class SeriesSubscriber implements Observable.OnSubscribeFunc<Span> {
	    private Subscription subscription;
	    private Node subscribedNode;
	    
	    private SeriesSubscriber(Subscription subscription, Node node) {
	        this.subscribedNode = node;
	        this.subscription = subscription;
	    }
	    @SuppressWarnings("unchecked")
        @Override
        public rx.Subscription onSubscribe(Observer<? super Span> t1) {
	    	subscribers.put(this.subscription, (Observer<Span>)t1);
	        this.subscribedNode.startUp();
	        
	        for(Distributor d : subscriberAssemblers.get(subscription)) {
	            System.out.println("registering assembler: " + d);
	            feedService.registerAssembler(d);
	        }
	        return null;
        }
	    
	}
}
