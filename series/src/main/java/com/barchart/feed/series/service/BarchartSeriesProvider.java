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
import com.barchart.feed.api.series.service.Assembler;
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
		Observable observable = publishNetwork(query, descriptor);
		return (TimeSeriesObservable)observable;
	}
	
	public Observable publishNetwork(Query query, NodeDescriptor descriptor) {
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
	
	public NodeDescriptor lookupDescriptor(Query query) {
	    NodeDescriptor descriptor;
	    String specifier = query.getAnalyticSpecifier();
	    if(specifier == null || specifier.equals(NodeType.IO.toString())) {
	        descriptor = new BarBuilderNodeDescriptor();
	    }else if(NetworkSchema.hasNetworkByName(specifier)) {
	        descriptor = NetworkSchema.getNetwork(specifier);
	    }else{
	        descriptor = NetworkSchema.lookup(specifier, query.getPeriods().size());
	    }//Types left to implement are: Expression, Spread, Synthetic, Adhoc, Assembler(?)
	    
	    return descriptor;
	}
	
	public AnalyticNode getOrCreateNode(SeriesSubscription subscription, SeriesSubscription original, AnalyticNodeDescriptor desc) {
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
	                AnalyticNode ioNode = (AnalyticNode)getOrCreateIONode(sub, original); 
	                ioNode.addChildNode(searchNode);
	                searchNode.addParentNode(ioNode);
	                ioNode.addOutputKeyMapping(key, sub);
	                searchNode.addInputTimeSeries(sub, ioNode.getOutputTimeSeries(sub));
	            }else{
	                AnalyticNode priorNode = this.getOrCreateNode(sub, original, parentDesc);
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
	
	/**
	 * 
	 * @param subscription
	 * @param original
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	Node getOrCreateIONode(SeriesSubscription subscription, SeriesSubscription original) {
	    boolean isAssembler = subscription.getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString());
	    
	    List<Node<SeriesSubscription>> derivables = findMatchingIONodes(subscription);
	    Node retVal = derivables.size() == 1 && 
	    	(retVal = derivables.get(0)).getOutputSubscriptions().contains(subscription) ? retVal : null;
	    
	    if(!isAssembler && (retVal == null || retVal.getParentNodes().isEmpty())) {
	        if(derivables.size() > 0 && retVal == null) {
	        	AnalyticNode derivableNode = (AnalyticNode)getBestDerivableNode(derivables, subscription);//Need algorithm to determine best derivable node
	        	retVal = linkBestDerivableIONodeToNewChain(derivableNode, subscription);
            } else if(retVal == null) {
                BarBuilderNodeDescriptor bbDesc = new BarBuilderNodeDescriptor();
                bbDesc.setAnalyticClass(BarBuilder.class);
                bbDesc.setConstructorArg(subscription);
                retVal = new AnalyticNode(bbDesc.instantiateBuilderAnalytic());
            }
	        
	        ((AnalyticNode)retVal).addOutputKeyMapping(BarBuilder.OUTPUT_KEY, subscription);
            List<SeriesSubscription> inputs = retVal.getInputSubscriptions();
            for(SeriesSubscription s : inputs) {
                Node n = getOrCreateIONode(s, original);
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
        	retVal = createIONode(subscription);
        	addAssembler(original, (Assembler)retVal);
        }
	    
	    return retVal;
	}
	
	/**
	 * Adds the specified {@link Assembler} to the list of assemblers
	 * and adds a mapping from the original Subscription being fetched
	 * and the assembler being created.
	 * 
	 * @param s		the original {@link SeriesSubscription} being fetched.
	 * @param a		the {@link Assembler} to add.
	 */
	@SuppressWarnings("unchecked")
	private void addAssembler(SeriesSubscription s, Assembler a) {
		assemblers.add((Node<SeriesSubscription>) a);
    	
    	List<Distributor> dists = null;
    	if((dists = subscriberAssemblers.get(s)) == null) {
    	    subscriberAssemblers.put(s, dists = new ArrayList<Distributor>());
    	}
    	dists.add((Distributor) a);
	}
	
	/**
	 * Returns a flag indicating whether the specified {@link Node} is 
	 * of type {@link Assembler}.
	 * 
	 * @param n		the node to test.
	 * @return		true if so, false if not.
	 */
	private boolean isAssembler(Node<SeriesSubscription> n) {
	    return ((SeriesSubscription)n.getOutputSubscriptions().get(0)).
	        getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString());
	}
	
	/**
	 * Creates and returns a {@link Node} of type {@link NodeType#IO} or
	 * {@link NodeType#ASSEMBLER}.
	 * 
	 * @param s		the {@link SeriesSubscription} whose analyticSpecifier
	 * 				field specifies the type of underlying node.
	 * @return		the Node of the specified type.
	 */
	private Node<SeriesSubscription> createIONode(SeriesSubscription s) {
		Node<SeriesSubscription> retVal = null;
		if(s.getAnalyticSpecifier().equals(NodeType.IO.toString())) {
			BarBuilderNodeDescriptor bbDesc = new BarBuilderNodeDescriptor();
            bbDesc.setAnalyticClass(BarBuilder.class);
            bbDesc.setConstructorArg(s);
            retVal = new AnalyticNode(bbDesc.instantiateBuilderAnalytic());
		}else if(s.getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString())) {
			retVal = new Distributor(s);
		}
		return retVal;
	}
	
	/**
	 * Because "derivable" nodes are not always an exact match, but are a node
	 * from which other nodes can be built (derived) due to their compatible 
	 * {@link Period}s, we must build those "in-between" nodes to account for the
	 * Period stepping necessary to arrive at the desired endpoint.
	 * 
	 * This method produces those required in-between nodes, connects them and adds
	 * them to the list of nodes so that they may be "looked up" and found. 
	 * 
	 * Finally, this method returns a reified node representing the final endpoint
	 * specified by the {@link SeriesSubscription} passed in. 
	 * @param derivableNode
	 * @param subscription
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Node<SeriesSubscription> linkBestDerivableIONodeToNewChain(AnalyticNode derivableNode, SeriesSubscription subscription) {
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
	
	/**
	 * Returns the "optimum" derivable node from a list of derivable nodes.
	 * TODO: Better implement this...
	 * 
	 * @param derivableNodes		the list of derivable nodes all of which could
	 * 								work as a node source.
	 * @param subscription			the subscription which could be sourced by the 
	 * 								the specified derivables.
	 * @return						the optimum derivable node based on pathway efficiency
	 * 								{@link TimeFrame} {@link Period} ratios etc.
	 */
	private Node<SeriesSubscription> getBestDerivableNode(List<Node<SeriesSubscription>> derivableNodes, Subscription subscription) {
	    Node<SeriesSubscription> n = derivableNodes.get(0);//Optimize this later
	    return n;
	}
	
	
	//////////////////////////////////////////////// Inner Class Definitions ////////////////////////////////////////////////
	
	
	/**
	 * Special descriptor for use as a key for lookups when searching for
	 * {@link AnalyticNode}s. This class has a specialized {@link #equals(Object)}
	 * method which ignores the {@link TimeFrame} ordering of the underlying {@link SeriesSubscription}'s
	 * {@code TimeFrame}s.
	 */
	static class SearchDescriptor {
	    /** the class of the analytic */
	    private Class<?> analyticClass;
	    
	    /** the arguments for the analytic's constructor */
	    private int[] args;
	    
	    /** the symbol of data */
	    private String symbol;
	    
	    /** the timeframes of data */
	    private TimeFrame[] timeFrames = new TimeFrame[0];
	    
	    private Map<SeriesSubscription,String> inputKeyMap = new HashMap<SeriesSubscription,String>();
	    
	    public SearchDescriptor(SeriesSubscription sub, AnalyticNodeDescriptor desc) {
	        this.loadFromSubscription(sub, desc);
	    }
	    
	    /**
	     * Loads the distinguishing fields from the specified {@link SeriesSubscription}.
	     * 
	     * @param subscription		the subscription supply the distinguishing search fields.
	     * @param desc				the descriptor indexed by this {@code SearchDescriptor}.
	     */
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
