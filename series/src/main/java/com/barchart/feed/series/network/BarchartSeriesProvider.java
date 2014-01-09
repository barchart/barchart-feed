package com.barchart.feed.series.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.series.TimeFrame;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.DataSeries;
import com.barchart.feed.api.series.TimeSeriesObservable;
import com.barchart.feed.api.series.network.Analytic;
import com.barchart.feed.api.series.network.Assembler;
import com.barchart.feed.api.series.network.NetworkDescriptor;
import com.barchart.feed.api.series.network.Node;
import com.barchart.feed.api.series.network.NodeDescriptor;
import com.barchart.feed.api.series.network.NodeType;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.network.Subscription;
import com.barchart.feed.api.series.service.SeriesFeedService;
import com.barchart.feed.series.DataPointImpl;
import com.barchart.feed.series.DataSeriesImpl;
import com.barchart.feed.series.TimeFrameImpl;
import com.barchart.feed.series.analytics.BarBuilder;

/**
 * Queryable framework for providing {@link DataSeries} objects.
 * 
 * @author David Ray
 */
public class BarchartSeriesProvider {
	private SeriesFeedService feedService;
	
	/** Contains output-level/Subscribable IO nodes */
	private List<Node<SeriesSubscription>> ioNodes = Collections.synchronizedList(new ArrayList<Node<SeriesSubscription>>());
	/** Contains output-level/Subscribable {@link Analytic} nodes */
    private List<Node<SeriesSubscription>> assemblers = Collections.synchronizedList(new ArrayList<Node<SeriesSubscription>>());
	/** Subscribers for a particular {@link Subscription} */
    private Map<Subscription, Observer<Span>> subscribers = new HashMap<Subscription, Observer<Span>>();
    private Map<Subscription, List<Distributor>> subscriberAssemblers = new HashMap<Subscription, List<Distributor>>();
    /** Contains all instantiated Nodes mapped to {@link SearchDescriptor}s */
    private Map<SearchDescriptor,AnalyticNode> searchMap = Collections.synchronizedMap(new HashMap<SearchDescriptor,AnalyticNode>());
    /** Monitor for the {@link #searchMap} */
    private Object searchLock = new Object();
	
	
	/**
	 * Instantiates a new {@code BarchartSeriesProvider}
	 * @param feedService	an implementation of {@link SeriesFeedService}
	 */
	public BarchartSeriesProvider(SeriesFeedService feedService) {
		this.feedService = feedService;
	}
	
	/**
	 *
	 * @param query
	 * @return
	 */
	public <T extends DataPoint> TimeSeriesObservable fetch(final Query query) {
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
		Observable observable = getObservable((SeriesSubscription)query.toSubscription(inst), descriptor);
		return (TimeSeriesObservable)observable;
	}
	
	public Observable getObservable(SeriesSubscription subscription, NodeDescriptor descriptor) {
        switch(descriptor.getType()) {
            case IO: {
                Node<SeriesSubscription> node = getOrCreateIONode(subscription, subscription);
                break;
            }
            case ANALYTIC: {
                Node<SeriesSubscription> node = getOrCreateNode(subscription, subscription, (AnalyticNodeDescriptor)descriptor);
                break;
            }
            case NETWORK: {
                NetworkDescriptor schema = (NetworkDescriptor)descriptor;
                List<Node<SeriesSubscription>> subscriptionNodes = new ArrayList<Node<SeriesSubscription>>();
                for(NodeDescriptor desc : schema.getMainPublishers()) {
                	Node<SeriesSubscription> newNode = getOrCreateNode(
                	    subscription, subscription, (AnalyticNodeDescriptor)desc);
                    subscriptionNodes.add(newNode);
                }
                break;
            }
            default: {
                //No other implementations for now...
            }
        }
        return null;
    }
	
	/**
	 * Returns a {@link NodeDescriptor} of the type necessary to produce the
	 * data described by the specified {@Query}.
	 * 
	 * @param query        the request object describing the desired output.
	 * @return             a {@link NodeDescriptor} of the type necessary to produce the
     *                     data described by the specified {@Query}
	 */
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
	
	/**
	 * Recursively calls itself to create or link created nodes to produce the 
	 * graph of nodes necessary to obtain the output data specified by the
	 * {@link SeriesSubscription} passed in. 
	 * <p>
	 * The returned node, (though not started) is fully configured and connected to all 
	 * required resources, and will produce the data specified by the subscription. 
	 * <p>
	 * This method successively requests the required inputs of each of it's ancestor nodes 
	 * as it locates or creates the ancestor nodes necessary to produced the input of each 
	 * preceding node until it reaches the top ({@link Assembler} or {@link Distributor}) 
	 * node which becomes the last node created or linked in if one with the required data 
	 * already exists.
	 * 
	 * @param subscription     the subscription describing the required output.
	 * @param original         the same subscription as the above subscription.
	 * @param desc             an {@link AnalyticNodeDescriptor} which serves as the schema
	 *                         of the returned node.
	 * @return                 a fully functional source of the data described by the specified
	 *                         {@link SeriesSubscription}
	 */
	public AnalyticNode getOrCreateNode(SeriesSubscription subscription, SeriesSubscription original, AnalyticNodeDescriptor desc) {
	    AnalyticNode searchNode = null;
	    SearchDescriptor searchKey = new SearchDescriptor(subscription, desc);
	    //SearchDescriptor's equals() method altered to be independent of TimeFrame[] ordering.
	    if((searchNode = lookupSearchDescriptor(searchKey)) == null) { 
	        searchNode = new AnalyticNode(desc.instantiateAnalytic());
	        mapSearchDescriptor(searchKey, searchNode);
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
	
	/**
	 * Returns a list of either derivable nodes or a single node which
	 * exactly matches the specified {@link SeriesSubscription}'s constraints.
	 * 
	 * Derivable nodes are nodes which produce data that is compatible with the 
	 * specified subscription and can be further modified to produce the exact
	 * data required by the subscription.
	 * 
	 * @param subscription     the subscription describing the data a matching node
	 *                         would have to match.
	 * @return                 a list of derivable nodes, a single matching node or
	 *                         an empty list. This method never returns null.
	 */
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
	 * This method recursively calls itself to build and/or locate existing nodes
	 * which exist in a chain of nodes which successively modify data to produce the
	 * time frame of data requested.
	 * 
	 * It returns the last leaf node of a connected chain of IO nodes, configured to
	 * produce the {@link SeriesSubscription} specified. The returned node can
	 * be connected to other nodes and supply them with the series data configured
	 * with the time constraints specified as required by their input specifications.
	 * 
	 * IO nodes are analytic nodes that are specialized to receive one input and produce
	 * a single output which modifies the data according to the time constraints of that
	 * particular IO node.
	 * 
	 * @param subscription         the subscription describing the required output.
	 * @param original             the original subscription, as this method recursively calls
	 *                             itself to build or attach to a supporting network, altering
	 *                             the first subscription parameter with each call.
	 * @return
	 */
	Node<SeriesSubscription> getOrCreateIONode(SeriesSubscription subscription, SeriesSubscription original) {
	    Node<SeriesSubscription> retVal = null;
	    
	    boolean isAssembler = subscription.getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString());
	    if(isAssembler) {
            retVal = createIONode(subscription);
            addAssembler(original, (Assembler)retVal);
        }else if((retVal = findDerivableIONode(subscription)) == null || !hasAssemblerParent(retVal)) {
            retVal = retVal == null ? createIONode(subscription) : retVal;
            ((AnalyticNode)retVal).addOutputKeyMapping(BarBuilder.OUTPUT_KEY, subscription);
            List<SeriesSubscription> inputs = retVal.getInputSubscriptions();
            for(SeriesSubscription s : inputs) {
                Node<SeriesSubscription> n = getOrCreateIONode(s, original);
                n.addChildNode(retVal);
                retVal.addParentNode(n);
                ((AnalyticNode)retVal).addInputKeyMapping(BarBuilder.INPUT_KEY, (SeriesSubscription)n.getOutputSubscriptions().get(0));
                if(!isAssembler(n)) {
                    ioNodes.add(retVal);
                    ((AnalyticNode)n).addOutputKeyMapping(BarBuilder.OUTPUT_KEY, s);
                    ((AnalyticNode)retVal).addInputTimeSeries(s, (DataSeriesImpl<DataPointImpl>)((AnalyticNode)n).getOutputTimeSeries(s));
                }else{
                    ((AnalyticNode)retVal).addInputTimeSeries(s, (DataSeriesImpl<DataPointImpl>)((Distributor)n).getOutputTimeSeries(s));
                }
            }
        }
	    
	    return retVal;
	}
	
	/**
	 * Returns a flag indicating whether the specified <em>IO</em> {@link Node} has
	 * an {@link Assembler} parent.
	 * 
	 * @param      n   the node to check for assembler parentage
	 * @return     true if so, false if not
	 */
	boolean hasAssemblerParent(Node<SeriesSubscription> n) {
	    if(!n.getOutputSubscriptions().get(0).getAnalyticSpecifier().equals(NodeType.IO.toString()) &&
	        !n.getOutputSubscriptions().get(0).getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString())) {
	        throw new IllegalArgumentException("Can only check for assembler parentage of a node with a single input");
	    }
	    Node<SeriesSubscription> parent = n;
	    do {
	        if(parent.getOutputSubscriptions().get(0).getAnalyticSpecifier().equals(NodeType.ASSEMBLER.toString())) {
	            return true;
	        }
	    } while((parent = parent.getParentNodes().get(0)) != null);
	    
	    return false;
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
		if(s == null) {
			throw new IllegalArgumentException("Attempt to use a null subscription as key for Assembler storage.");
		}else if(a == null) {
			throw new IllegalArgumentException("Attempt to store a null Assembler for subscription: " + s);
		}
		
		if(!assemblers.contains(a)) {
			assemblers.add((Node<SeriesSubscription>) a);
		}
    	
    	List<Distributor> dists = null;
    	if((dists = subscriberAssemblers.get(s)) == null) {
    	    subscriberAssemblers.put(s, dists = new ArrayList<Distributor>());
    	}
    	if(!dists.contains(a)) {
    		dists.add((Distributor) a);
    	}
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
	 * Provides thread-safe access to the map of live nodes.
	 * 
	 * @param sd       a {@link SearchDescriptor} describing the required node to return.
	 * @return         the matching {@link Node} if it exists.
	 */
	private AnalyticNode lookupSearchDescriptor(SearchDescriptor sd) {
	    synchronized(searchLock) {
	        return searchMap.get(sd);
	    }
	}
	
	/**
	 * Stores a mapping of {@link SearchDescriptor} to the specified {@link AnalyticNode}
	 * 
	 * @param sd       a {@link SearchDescriptor} describing the required node to store.
	 * @param nodeToStore      the node to store.
	 */
	private void mapSearchDescriptor(SearchDescriptor sd, AnalyticNode nodeToStore) {
	    synchronized(searchLock) {
	        searchMap.put(sd, nodeToStore);
	    }
	}
	
	/**
     * Returns a {@link Node} which is directly or indirectly attached to
     * a node whose output data is sufficient to act as input to a node
     * specified by the {@link SeriesSubscription} passed in.
     * 
     * @param      subscription    the Subscription specifying the node to return.
     * @return     the specified node or null if no node outputs derivable data as
     *             specified by the Subscription passed in.
     */
    private Node<SeriesSubscription> findDerivableIONode(SeriesSubscription subscription) {
        Node<SeriesSubscription> retVal = null;
        
        List<Node<SeriesSubscription>> derivables = findMatchingIONodes(subscription);
        if(derivables.size() > 0) {
            retVal = derivables.size() == 1 && 
                (retVal = derivables.get(0)).getOutputSubscriptions().contains(subscription) ? retVal : null;
            
            if(retVal == null) {
                AnalyticNode derivableNode = (AnalyticNode)getBestDerivableNode(derivables, subscription);//Need algorithm to determine best derivable node
                retVal = linkBestDerivableIONodeToNewChain(derivableNode, subscription);
            }
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
        List<AnalyticNode> nodeChain = getNodeChain(derivableSubscription, subscription);
        for(int i = 0;i < nodeChain.size();i++) {
            AnalyticNode child = nodeChain.get(i);
            ioNodes.add(child);
            derivableNode.addChildNode(child);
            child.addParentNode(derivableNode);
            child.addInputTimeSeries(child.getInputSubscriptions().get(0), 
                (DataSeriesImpl)derivableNode.getOutputTimeSeries(derivableNode.getOutputSubscriptions().get(0)));
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
	 * 								{@link TimeFrameImpl} {@link Period} ratios etc.
	 */
	private Node<SeriesSubscription> getBestDerivableNode(List<Node<SeriesSubscription>> derivableNodes, Subscription subscription) {
	    Node<SeriesSubscription> n = derivableNodes.get(0);//Optimize this later
	    if(!n.getOutputSubscriptions().contains(subscription)) {
	        for(Node<SeriesSubscription> search : derivableNodes) {
	            if(search.getOutputSubscriptions().get(0).isCloserTo(subscription, n.getOutputSubscriptions().get(0))) {
	                n = search;
	            }
	        }
	    }
	    return n;
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
        sSub.setTimeFrames(new TimeFrameImpl[] {
            new TimeFrameImpl(next, input.getTimeFrames()[0].getStartDate(), input.getTimeFrames()[0].getEndDate()) });
        BarBuilderNodeDescriptor descriptor = new BarBuilderNodeDescriptor();
        descriptor.setAnalyticClass(BarBuilder.class);
        descriptor.setConstructorArg(sSub);
        AnalyticNode bb = new AnalyticNode(descriptor.instantiateBuilderAnalytic());
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
     *           the first parameter, to the Node whose output {@link Subscription} is the second.
     */
     public List<AnalyticNode> getNodeChain(Subscription derivableSubscription, Subscription subscriptionTarget) {
        if(derivableSubscription == null || subscriptionTarget == null || derivableSubscription.equals(subscriptionTarget)) {
            throw new IllegalArgumentException("Source and target cannot be null or equal to each other.");
        }
        
        SeriesSubscription higher = (SeriesSubscription)subscriptionTarget;
        SeriesSubscription lower = (SeriesSubscription)derivableSubscription;
        
        List<AnalyticNode> retVal = new ArrayList<AnalyticNode>();
        BarBuilderNodeDescriptor descriptor = new BarBuilderNodeDescriptor();
        descriptor.setAnalyticClass(BarBuilder.class);
        descriptor.setConstructorArg(higher);
        AnalyticNode bb = new AnalyticNode(descriptor.instantiateBuilderAnalytic());
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
            (smallerType = BarBuilderNodeDescriptor.getLowerBaseType(smallerType)) != lower.getTimeFrames()[0].getPeriod().getPeriodType()) {
            Period smaller = new Period(smallerType, 1);
            higher = addNextNodeToChain(retVal, higher, smaller);
        }
        
        //Reverse so that processors ascend in period type
        Collections.reverse(retVal);
        
        return retVal;
    }
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                               Inner Class Definitions                                               //
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Special descriptor for use as a key for lookups when searching for
	 * {@link AnalyticNode}s. This class has a specialized {@link #equals(Object)}
	 * method which ignores the {@link TimeFrameImpl} ordering of the underlying {@link SeriesSubscription}'s
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
	     * Loads the distinguishing fields from the specified {@link SeriesSubscription} and
	     * {@link AnalyticNodeDescriptor}
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
	    private SeriesSubscription subscription;
	    private Node<SeriesSubscription> subscribedNode;
	    
	    public SeriesSubscriber(SeriesSubscription subscription, Node<SeriesSubscription> node) {
	        this.subscribedNode = node;
	        this.subscription = subscription;
	    }
	    
	    public SeriesSubscriber(SeriesSubscription subscription, List<Node<SeriesSubscription>> publishers) {
	    	
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
