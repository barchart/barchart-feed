package com.barchart.feed.series.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.service.FeedMonitorService;
import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.NodeDescriptor;
import com.barchart.feed.api.series.service.NodeType;
import com.barchart.feed.api.series.service.Query;
import com.barchart.feed.api.series.service.Subscription;
import com.barchart.feed.api.series.temporal.TimeFrame;
import com.barchart.feed.series.DataSeries;
import com.barchart.feed.series.analytics.BarBuilder;

/**
 * Queryable framework for providing {@link TimeSeries} objects.
 * 
 * @author David Ray
 */
public class BarchartSeriesProvider {
	private FeedMonitorService feedService;
	
	/** Contains output-level/Subscribable IO nodes */
	private List<Node<SeriesSubscription>> ioNodes = Collections.synchronizedList(new ArrayList<Node<SeriesSubscription>>());
	/** Contains output-level/Subscribable {@link Analytic} nodes */
    private List<Node<SeriesSubscription>> assemblers = Collections.synchronizedList(new ArrayList<Node<SeriesSubscription>>());
	/** Subscribers for a particular {@link Subscription} */
    private Map<Subscription, Observer<Span>> subscribers = new HashMap<Subscription, Observer<Span>>();
    private Map<Subscription, List<Distributor>> subscriberAssemblers = new HashMap<Subscription, List<Distributor>>();
    /** Contains all instantiated Nodes mapped to {@link SearchDescriptor}s */
    private Map<SearchDescriptor,AnalyticNode> searchMap = Collections.synchronizedMap(new HashMap<SearchDescriptor,AnalyticNode>());
	
	
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
		Instrument inst = feedService.lookupInstrument(query.getSymbols().get(0)); //Supports multiple symbols for spreads/expressions
		
//		SeriesSubscription subscription = (SeriesSubscription)query.toSubscription(inst);
//		System.out.println("inst = " + inst.symbol());
//		AnalyticNode node = lookupNode(subscription, subscription);
//		DataSeries<?> series = node.getOutputTimeSeries(subscription);
//		
//		return (new TimeSeriesObservable(new SeriesSubscriber(subscription, node), series) {
//		    @SuppressWarnings("unchecked")
//            public TimeSeries<?> getTimeSeries() { return (TimeSeries<TimePoint>)this.series; }
//		});
		
		NodeDescriptor descriptor = lookupDescriptor(query);
		Observable observable = lookup(query, descriptor);
		return (TimeSeriesObservable)observable;
	}
	
	public Observable lookup(Query query, NodeDescriptor descriptor) {
        switch(descriptor.getType()) {
            case IO: {
                
            }
            case ANALYTIC: {
                
            }
            case NETWORK: {
                
            }
            default: {
                //No other implementations for now...
            }
        }
        return null;
    }
	
	private NodeDescriptor lookupDescriptor(Query query) {
	    NodeDescriptor descriptor;
	    String specifier = query.getAnalyticSpecifier();
	    if(specifier == null) {
	        descriptor = new BarBuilderNodeDescriptor();
	    }else if(NetworkSchema.hasNetworkByName(specifier)) {
	        descriptor = NetworkSchema.getNetwork(specifier);
	    }else{
	        descriptor = NetworkSchema.lookup(specifier, query.getPeriods().size());
	    }
	    
	    return descriptor;
	}
	
	
	
	public AnalyticNode getNode(SeriesSubscription subscription, AnalyticNodeDescriptor desc) {
	    AnalyticNode searchNode = null;
	    SearchDescriptor searchKey = new SearchDescriptor(subscription, desc);
	    //SearchDescriptor's equals() method altered to be independent of TimeFrame[] ordering.
	    if((searchNode = searchMap.get(searchKey)) == null) { 
	        searchNode = new AnalyticNode(desc.instantiateAnalytic());
	        searchMap.put(searchKey, searchNode);
	        searchNode.addOutputKeyMapping(desc.getOutputKey(), subscription);
	        Map<String, SeriesSubscription> requiredSubs = desc.getRequiredSubscriptions(subscription);
	        for(String key : requiredSubs.keySet()) {
	            SeriesSubscription sub = requiredSubs.get(key);
	            searchNode.addInputKeyMapping(key, sub);
	            AnalyticNodeDescriptor parentDesc = NetworkSchema.lookup(sub.getAnalyticSpecifier(), sub.getTimeFrames().length);
	            if(parentDesc == null) {
	                AnalyticNode ioNode = (AnalyticNode)lookupIONode(sub, sub); 
	                ioNode.addChildNode(searchNode);
	                searchNode.addParentNode(ioNode);
	                ioNode.addOutputKeyMapping(key, sub);
	                searchNode.addInputTimeSeries(sub, ioNode.getOutputTimeSeries(sub));
	            }else{
	                AnalyticNode priorNode = this.getNode(sub, parentDesc);
                    priorNode.addChildNode(searchNode);
                    searchNode.addParentNode(priorNode);
                    priorNode.addOutputKeyMapping(parentDesc.getOutputKey(), sub);
                    searchNode.addInputTimeSeries(sub, priorNode.getOutputTimeSeries(sub));
	            }
	        }
	    }
	    return searchNode;
	}
	
	private List<Node<SeriesSubscription>> findMatchingIONodes(SeriesSubscription subscription) {
	    List<Node<SeriesSubscription>> derivables = new ArrayList<Node<SeriesSubscription>>();
        for(Node<SeriesSubscription> node : ioNodes) {
            if(node.getOutputSubscriptions().contains(subscription)) {
                derivables.clear();
                derivables.add(node);
                break;
            }else if(node.isDerivableSource(subscription)) {
                derivables.add(node);
            }
        }
        return derivables;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	Node lookupIONode(SeriesSubscription subscription, SeriesSubscription original) {
	    boolean isAssembler = subscription.getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString());
	    
	    Node retVal = null;
	    List<Node<SeriesSubscription>> derivables = null;
	    for(Node node : ioNodes) {
            if(node.getOutputSubscriptions().contains(subscription)) {
                retVal = (AnalyticNode)node; 
                break;
            }else if(node.isDerivableSource(subscription)) {
                if(derivables == null) {
                    derivables = new ArrayList<Node<SeriesSubscription>>();
                }
                derivables.add(node);
            }
        }
	    
	    if(!isAssembler && (retVal == null || retVal.getParentNodes().isEmpty())) {
	        if(derivables != null && retVal == null) {
	        	retVal = linkBestDerivableIONodeToNewChain(derivables, subscription);
            } else if(retVal == null) {
                BarBuilderNodeDescriptor bbDesc = new BarBuilderNodeDescriptor();
                bbDesc.setAnalyticClass(BarBuilder.class);
                bbDesc.setConstructorArg(subscription);
                retVal = new AnalyticNode(bbDesc.instantiateBuilderAnalytic());
            }
	        
	        ((AnalyticNode)retVal).addOutputKeyMapping(BarBuilder.OUTPUT_KEY, subscription);
            List<SeriesSubscription> inputs = retVal.getInputSubscriptions();
            for(SeriesSubscription s : inputs) {
                Node n = lookupIONode(s, original);
                n.addChildNode(retVal);
                retVal.addParentNode(n);
                ((AnalyticNode)retVal).addInputKeyMapping(BarBuilder.INPUT_KEY, (SeriesSubscription)n.getOutputSubscriptions().get(0));
                if(!isAssembler(n)) {
                    ioNodes.add(retVal);
                    ((AnalyticNode)n).addOutputKeyMapping(BarBuilder.OUTPUT_KEY, s);
                    ((AnalyticNode)retVal).addInputTimeSeries(s, (DataSeries)((AnalyticNode)n).getOutputTimeSeries(s));
                }else{
                    ((AnalyticNode)retVal).addInputTimeSeries(s, (DataSeries)((Distributor)n).getOutputTimeSeries(s));
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
	
	private boolean isExactMatch(Node<SeriesSubscription> n, SeriesSubscription s) {
	    return n.getOutputSubscriptions().contains(s);
	}
	
	private boolean isAssembler(Node<SeriesSubscription> n) {
	    return ((SeriesSubscription)n.getOutputSubscriptions().get(0)).
	        getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Node<SeriesSubscription> linkBestDerivableIONodeToNewChain(List<Node<SeriesSubscription>> derivables, SeriesSubscription subscription) {
		AnalyticNode derivableNode = (AnalyticNode)getBestDerivableNode(derivables, subscription);//Need algorithm to determine best derivable node
		
        SeriesSubscription derivableSubscription = derivableNode.getDerivableOutputSubscription(subscription);
        if(derivableSubscription == null) {
            throw new IllegalStateException("Could not find derivable output on a node deemed to be a derivable source. " + derivableSubscription);
        }
        List<AnalyticNode> nodeChain = new BarBuilderNodeDescriptor().getNodeChain(derivableSubscription, subscription);
        for(int i = 0;i < nodeChain.size();i++) {
            AnalyticNode child = nodeChain.get(i);
            ioNodes.add(child);
            derivableNode.addChildNode(child);
            child.addParentNode(derivableNode);
            child.addInputTimeSeries(child.getInputSubscriptions().get(0), 
                (DataSeries)derivableNode.getOutputTimeSeries(derivableNode.getOutputSubscriptions().get(0)));
            derivableNode = child;
        }
        
        return derivableNode;
	}
	
	static class SearchDescriptor {
	    /** the class of the analytic */
	    private Class<?> analyticClass;
	    
	    /** the arguments for the analytic's constructor */
	    private int[] args;
	    
	    /** the symbol of data */
	    private String symbol;
	    
	    /** the timeframes of data */
	    private TimeFrame[] timeFrames = new TimeFrame[0];
	    
	    private HashMap<SeriesSubscription,String> inputKeyMap;
	    
	    public SearchDescriptor(SeriesSubscription sub, AnalyticNodeDescriptor desc) {
	        this.loadFromSubscription(sub, desc);
	    }
	    
	    public void loadFromSubscription(SeriesSubscription subscription, AnalyticNodeDescriptor desc) {
	        if (desc != null) {
	            this.analyticClass = desc.getAnalyticClass();
	            this.args = desc.getConstructorArgs();
	            desc.loadSearchDescriptor(this, subscription);
	        }
	        this.symbol = subscription.getSymbol();
	        this.timeFrames = subscription.getTimeFrames();
	    }
	    
	    /**
	     * Reverse mapping from {@link Subscription}s to input keys
	     * @param key
	     * @param sub
	     */
	    public void addInputKeyMapping(String key, SeriesSubscription sub) {
	        this.inputKeyMap.put(sub, key);
	    }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((analyticClass == null) ? 0 : analyticClass.hashCode());
            result = prime * result + Arrays.hashCode(args);
            result = prime * result
                    + ((inputKeyMap == null) ? 0 : inputKeyMap.hashCode());
            result = prime * result
                    + ((symbol == null) ? 0 : symbol.hashCode());
            result = prime * result + Arrays.hashCode(timeFrames);
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            SearchDescriptor other = (SearchDescriptor)obj;
            if(analyticClass == null) {
                if(other.analyticClass != null)
                    return false;
            } else if(!analyticClass.equals(other.analyticClass))
                return false;
            if(!Arrays.equals(args, other.args))
                return false;
            if(inputKeyMap == null) {
                if(other.inputKeyMap != null)
                    return false;
            } else if(!inputKeyMap.equals(other.inputKeyMap))
                return false;
            if(symbol == null) {
                if(other.symbol != null)
                    return false;
            } else if(!symbol.equals(other.symbol))
                return false;
            for(TimeFrame tf : timeFrames) { //Don't use order dependency like Arrays.equals()
                boolean found = false;
                for(TimeFrame tf2 : other.timeFrames) {
                    found = tf.equals(tf2);
                    if(found) break;
                }
                if(!found) return false;
            }
             
            return true;
        }
	}
	
	private Node<SeriesSubscription> getBestDerivableNode(List<Node<SeriesSubscription>> derivableNodes, Subscription subscription) {
	    Node<SeriesSubscription> n = derivableNodes.get(0);//Optimize this later
	    return n;
	}
	
	public class SeriesSubscriber implements Observable.OnSubscribeFunc<Span> {
	    private Subscription subscription;
	    private Node<SeriesSubscription> subscribedNode;
	    
	    private SeriesSubscriber(Subscription subscription, Node<SeriesSubscription> node) {
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
