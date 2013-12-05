package com.barchart.feed.series.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

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
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.client.provider.BarchartMarketProvider;

/**
 * Queryable framework for providing {@link TimeSeries} objects.
 * 
 * @author David Ray
 */
public class BarchartSeriesProvider {
	private MarketService marketProvider;
	private ConsumerAgent consumerAgent;
	private ObservableMonitor monitor;
	private MarketSubject market;
	
	Map<String, Observer<Pair<String, Object>>> symbolObservers =
		Collections.synchronizedMap(new HashMap<String, Observer<Pair<String, Object>>>());
	
	private Object waitMonitor = new Object();
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	
	
	
	/**
	 * Instantiates a new {@code BarchartSeriesProvider}
	 * @param service	an implementation of {@link MarketService} such as 
	 * 					{@link BarchartMarketProvider}
	 */
	public BarchartSeriesProvider(MarketService service) {
		this.marketProvider = service;
		
		startAndMonitorConnection();
	}
	
	/**
	 *
	 * @param query
	 * @return
	 */
	public <T extends TimePoint> Observable<TimeSeries<T>> subscribe(Query query) {
		if(!isRunning.get()) {
			synchronized(waitMonitor) {
				try { waitMonitor.wait(); } catch(Exception e) { e.printStackTrace(); }
			}
		}
		
		Observable<TimeSeries<T>> returnVal = null;
		
		String symbol = query.getSymbol();
		Distributor distributor = new Distributor();
		symbolObservers.put(symbol, distributor);
		
		//Multi-map the instrument Distributor to all possible symbols for the specified symbol
		consumerAgent.include(symbol).subscribe(createInstrumentObserver(distributor));
		
		return null;
	}
	
	private Distributor lookupNode(String symbol) {
		return null;
	}
	
	private void startAndMonitorConnection() {
		monitor = createObservableConnectionStateMonitor();
		marketProvider.bindConnectionStateListener(monitor);
		marketProvider.startup();
		
		monitor.subscribe(getWaitForConnectionObserver());
	}
	
	private ObservableMonitor createObservableConnectionStateMonitor() {
		return new ObservableMonitor();
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
							isRunning.getAndSet(true);
							waitMonitor.notifyAll();
						}catch(Exception e) { e.printStackTrace(); }
					}
					
					market = new MarketSubject();
					consumerAgent = marketProvider.register(market, Market.class);
				}
			}
		};
	}
	
	private Observer<Result<Instrument>> createInstrumentObserver(final Distributor distributor) {
		return new Observer<Result<Instrument>>() {
			@Override
			public void onCompleted() {
				System.out.println("Lookup and registration complete");
			}
			@Override
			public void onError(Throwable e) {
				System.out.println("Exception in lookup and registration \n{} "+ e);
			}
			@Override
			public void onNext(Result<Instrument> args) {
				System.out.println("New Instrument Lookup and Registration " + args.results().keySet());
				
				for(List<Instrument> l : args.results().values()) {
					for(Instrument i : l) {
						symbolObservers.put(i.symbol(), distributor);
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
	 * Used internally to 
	 * @author David Ray
	 *
	 */
	private class MarketSubject implements MarketObserver<Market> {
		@Override
		public void onNext(final Market v) {
			if(v.change().contains(Component.TRADE)) {
				String symbol = v.trade().instrument().symbol();
				Pair<String, Object> pair = new Pair<String, Object>(symbol, v);
				
				symbolObservers.get(symbol).onNext(pair);
			}
			
		}
	}
	
	private class HistoricalSubject {
		List<Observer<? super Pair<String, Object>>> observers = 
			Collections.synchronizedList(new ArrayList<Observer<? super Pair<String, Object>>>());
	}
	
	/**
	 * Observable market service connection state monitor
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
		public Subscription subscribe(Observer<Pair<Connection, State>> observer) {
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
				public Subscription onSubscribe(final Observer<? super Pair<Connection, State>> t1) {
					observers.add(t1);
					
					return new Subscription() {
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
