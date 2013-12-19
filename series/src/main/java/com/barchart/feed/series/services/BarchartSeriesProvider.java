package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Observer;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection;
import com.barchart.feed.api.connection.Connection.Monitor;
import com.barchart.feed.api.connection.Connection.State;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.consumer.MetadataService.Result;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.Market.Component;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Analytic;
import com.barchart.feed.api.series.services.HistoricalObserver;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.HistoricalService;
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
	private MarketService marketService;
	private HistoricalService<HistoricalResult> historicalService;
	private ConsumerAgent consumerAgent;
	private ObservableMonitor monitor;
	private MarketSubject market;
	private HistoricalSubject historical;
	
	Map<InstrumentID, Distributor> symbolObservers =
		Collections.synchronizedMap(new HashMap<InstrumentID, Distributor>());
	
	private Object waitMonitor = new Object();
	private AtomicBoolean isConnected = new AtomicBoolean(false);
	
	/** Constains output-level/Subscribable IO nodes */
	private List<Node> ioNodes = Collections.synchronizedList(new ArrayList<Node>());
	/** Constains output-level/Subscribable {@link Analytic} nodes */
    private List<Node> analyticNodes = Collections.synchronizedList(new ArrayList<Node>());
	
	
	
	/**
	 * Instantiates a new {@code BarchartSeriesProvider}
	 * @param mktService	an implementation of {@link MarketService} such as {@link BarchartMarketProvider}
	 * @param histService	an implementation of {@link BarchartHistoricalService} such as {@link BarchartHistoricalProvider}
	 */
	public BarchartSeriesProvider(MarketService mktService, HistoricalService<HistoricalResult> histService) {
		this.marketService = mktService;
		this.historicalService = histService;
		startAndMonitorConnection();
	}
	
	/**
	 *
	 * @param query
	 * @return
	 */
	public <T extends TimePoint> Observable<TimeSeries<T>> fetch(final Query query) {
		if(!isConnected.get()) {
			synchronized(waitMonitor) {
				try { waitMonitor.wait(); } catch(Exception e) { e.printStackTrace(); }
			}
		}
		
		Instrument inst = lookupInstrument(query.getSymbol());
		SeriesSubscription subscription = (SeriesSubscription)query.toSubscription(inst);
		
		System.out.println("inst = " + inst.symbol());
		
		//The below map will not be used in the final implementation
		//due to the fact that there will be a graph lookup and matching
		//algorithm based on input/output subscriptions of nodes.
		symbolObservers.put(inst.id(), (Distributor)lookupNode(subscription));
		
		consumerAgent.include(inst);
		historicalService.subscribe(historical, subscription);
		
		return null;
	}
	
	private Instrument lookupInstrument(String symbol) {
		Result<Instrument> result = marketService.instrument(symbol).toBlockingObservable().single();
		return result.results().values().iterator().next().get(0);
	}
	
	private Node lookupNode(Subscription subscription) {
	    boolean isIO = subscription.getNodeDescriptor().getSpecifier().equals(NodeDescriptor.TYPE_IO);
	    List<Node> derivables = null;
	    Node derivableLookup = null;
	    Node retVal = null;
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
	    
	    if(retVal == null) {
	        if(derivables != null) {
	            Node derivableNode = getBestDerivableNode(derivables, subscription);//Need algorithm to determine best derivable node
	            Subscription derivableSubscription = derivableNode.getDerivableOutputSubscription(subscription);
                List<Processor> missingNodeLinks = subscription.getNodeDescriptor().getProcessorChain(derivableSubscription, subscription);
                for(Processor p : missingNodeLinks) {
                    switch(p.getCategory()) {
                        case ASSEMBLER: { break; }//Add container for Assemblers
                        case BAR_BUILDER: { ioNodes.add((BarBuilder)p); break; }
                        case ANALYTIC: { break; }//Add the AnalyticNode later...
                    }
                }
                
                //Still need to hookup the above added nodes
            }
	        
	        retVal = isIO ? new BarBuilder(subscription) : null;
            List<Subscription> inputs = retVal.getInputSubscriptions();
            for(Subscription s : inputs) {
                Node n = lookupNode(s);
                n.addChildNode(retVal, s);
            }
        }
	    
	    return retVal;
	}
	
	private Node getBestDerivableNode(List<Node> derivableNodes, Subscription subscription) {
	    Node n = derivableNodes.get(0);//Optimize this later
	    return n;
	}
	
	private void startAndMonitorConnection() {
		monitor = new ObservableMonitor();
		marketService.bindConnectionStateListener(monitor);
		marketService.startup();
		
		monitor.subscribe(getWaitForConnectionObserver());
	}
	
	private Observer<Pair<Connection, State>> getWaitForConnectionObserver() {
		return new Observer<Pair<Connection, State>>() {
			@Override public void onCompleted() {}
			@Override public void onError(Throwable e) {}
			@Override
			public void onNext(Pair<Connection, State> args) {
				System.out.println("New State = {} "+ args.last);
				if(args.last == State.CONNECTED) {
					synchronized(waitMonitor) {
						try { 
							isConnected.getAndSet(true);
							
							if(market == null) {
								market = new MarketSubject();
								consumerAgent = marketService.register(market, Market.class);
								historical = new HistoricalSubject();
							}
							
							waitMonitor.notify();
						}catch(Exception e) { e.printStackTrace(); }
					}
				}
			}
		};
	}
	
	/**
	 * Simple pair class to contain the results of a connection state 
	 * change notification. This is to be used with any clients wishing 
	 * to monitor this connection.
	 * 
	 * @author David Ray
	 *
	 * @param <K>
	 * @param <V>
	 * @see	ObservableMonitor#subscribe(Observer)
	 */
	static class Pair<K, V> {
		K first;
		V last;
		public Pair(K first, V last) {
			this.first = first;
			this.last = last;
		}
	}
	
	/**
	 * Observer which receives notification when the {@link Market} has
	 * been updated with a change of the previously registered "type(s)".
	 * Next this class routes the received data to the 
	 */
	private class MarketSubject implements MarketObserver<Market> {
		@Override
		public void onNext(final Market v) {
			if(v.change().contains(Component.TRADE)) {
				symbolObservers.get(v.trade().instrument().id()).onNextMarket(v);
			}
		}
	}
	
	/**
	 * {@link Observable} which is responsible for requesting historical data
	 * based on multiple criteria and routing that data to the receiver based on
	 * the registered query.
	 */
	private class HistoricalSubject implements HistoricalObserver<HistoricalResult> {
		@Override public void onCompleted() {}
		@Override public void onError(Throwable e) {}
		@Override
		public void onNext(HistoricalResult historicalResult) {
			symbolObservers.get(historicalResult.getSubscription().getInstrument().id()).onNextHistorical(historicalResult);
		}
	}
	
	/**
	 * {@link Observable} market service connection state monitor
	 */
	private class ObservableMonitor implements Monitor {
		List<Observer<? super Pair<Connection, State>>> observers = 
			Collections.synchronizedList(new ArrayList<Observer<? super Pair<Connection, State>>>());
		
		Observable.OnSubscribeFunc<Pair<Connection, State>> observable;
		
		/**
		 * Constructs a new ObservableMonitor
		 */
		ObservableMonitor() {
			this.observable = createSubscribeFunction();
		}
		
		/**
		 * Subscribes the specified {@link Observer} to this {@code ObservableMonitor}
		 * 
		 * @param observer	the {@code Observer} to subscribe.
		 * @return  {@link Subscription} to be used for unsubscribing.
		 */
		public rx.Subscription subscribe(Observer<Pair<Connection, State>> observer) {
			return observable.onSubscribe(observer);
		}
		
		/**
		 * Prepares the function handling all subscriptions and then subscribes to it.
		 * 
		 * @return	the subscribe function
		 */
		private Observable.OnSubscribeFunc<Pair<Connection, State>> createSubscribeFunction() {
			Observable.OnSubscribeFunc<Pair<Connection, State>> func = new Observable.OnSubscribeFunc<Pair<Connection, State>>() {
				@Override
				public SeriesSubscription onSubscribe(final Observer<? super Pair<Connection, State>> t1) {
					observers.add(t1);
					
					return new SeriesSubscription() {
						public void unsubscribe() {
							//????  This is probably warranted no?
							//marketProvider.unBindConnectionStateListener();
							observers.remove(t1);
						}
					};
				}
			};
			
			return func;
		}
		
		@Override
		public void handle(State state, Connection connection) {
			for(Observer<? super Pair<Connection, State>> o : observers) {
				o.onNext(new Pair<Connection, State>(connection, state));
			}
		}
	}
	
}
