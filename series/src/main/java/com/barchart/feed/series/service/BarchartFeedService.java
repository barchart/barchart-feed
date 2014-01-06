package com.barchart.feed.series.service;

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
import com.barchart.feed.api.series.service.Assembler;
import com.barchart.feed.api.series.service.FeedMonitorService;
import com.barchart.feed.api.series.service.HistoricalObserver;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.api.series.service.HistoricalService;
import com.barchart.feed.api.series.service.Subscription;

/**
 * Combined live quote data and historical data feed specialized to 
 * provide data to registered {@link Assemblers}. Instances of this 
 * class provide all supportive functions necessary to interact with
 * data supplying services, such as {@link Instrument} lookup and
 * register/unregister functions together with connection monitoring
 * and quote message specification.
 * 
 * @author David Ray
 */
public class BarchartFeedService implements FeedMonitorService {
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
	
	
	/**
	 * Instantiates a new {@code BarchartSeriesProvider}
	 * @param mktService	an implementation of {@link MarketService} such as {@link BarchartMarketProvider}
	 * @param histService	an implementation of {@link BarchartHistoricalService} such as {@link BarchartHistoricalProvider}
	 */
	public BarchartFeedService(MarketService mktService, HistoricalService<HistoricalResult> histService) {
		this.marketService = mktService;
		this.historicalService = histService;
		startAndMonitorConnection();
	}
	
	/**
	 * Returns the {@link Instrument} object whose symbol is the 
	 * symbol specified.
	 * 
	 * @param	symbol		the symbol for which to do an Instrument lookup
	 * @return	the Instrument found or null.
	 */
	@Override
	public Instrument lookupInstrument(String symbol) {
		Result<Instrument> result = marketService.instrument(symbol).toBlockingObservable().single();
		return result.results().values().iterator().next().get(0);
	}
	
	/**
	 * Stores the reference to the {@link Assembler} specified and activates
	 * its input data feed.
	 * 
	 * @param	assembler		the {@link Assembler} to register.
	 */
	@Override
	public void registerAssembler(Assembler assembler) {
		if(!isConnected.get()) {
			synchronized(waitMonitor) {
				try { waitMonitor.wait(); } catch(Exception e) { e.printStackTrace(); }
			}
		}
		
		SeriesSubscription subscription = (SeriesSubscription)assembler.getSubscription();
		symbolObservers.put(subscription.getInstrument().id(), (Distributor)assembler);
		consumerAgent.include(subscription.getInstrument());
		historicalService.subscribe(historical, subscription);
	}
	
	/**
	 * Removes the reference to the {@link Assembler} specified and deactivates
	 * its input data feed.
	 * 
	 * TODO: See note in method comment
	 * 
	 * @param	assembler		the {@link Assembler} to register.
	 */
	@Override
	public void unregisterAssembler(Assembler assembler) {
		SeriesSubscription subscription = (SeriesSubscription)assembler.getSubscription();
		symbolObservers.remove(subscription.getInstrument().id());
		//Excluding as in: consumerAgent.exclude(assember.getSubscription()) has to be carefully considered 
		//				   due to node sharing (we don't want to turn off the faucett if there are other down
		//				   stream nodes needing an instrument's raw data. TO BE DONE! Maybe maintain a CountDownLatch ?
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
