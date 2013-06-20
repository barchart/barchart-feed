package com.barchart.feed.api;

import com.barchart.feed.api.connection.ConnectionFuture;
import com.barchart.feed.api.connection.ConnectionLifecycle;
import com.barchart.feed.api.connection.ConnectionStateListener;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.OrderBook;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;

public interface Feed extends ConnectionLifecycle<Feed>, AgentBuilder {

	/* ***** ***** ConnectionLifecycle ***** ***** */
	
	@Override
	ConnectionFuture<Feed> startup();
	
	@Override
	ConnectionFuture<Feed> shutdown();
	
	/**
	 * Applications which need to react to the connectivity state of the feed
	 * instantiate a FeedStateListener and bind it to the client.
	 * 
	 * @param listener
	 *            The listener to be bound.
	 */
	@Override
	void bindConnectionStateListener(ConnectionStateListener listener);
	
	/**
	 * Applications which require time-stamp or heart-beat messages from the
	 * data server instantiate a DDF_TimestampListener and bind it to the
	 * client.
	 * 
	 * @param listener
	 */
	@Override
	void bindTimestampListener(TimestampListener listener);
	
	/* ***** ***** AgentBuilder ***** ***** */
	
	@Override
	<V extends MarketData<V>> Agent newAgent(Class<V> clazz, 
			MarketObserver<V> callback);
	
	/* ***** ***** Helper subscribe methods ***** ***** */
	
	
	// Agent a = feed.subscribe(new String[] { "IBM", "MSFT" }, MARKET, callback);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketObserver<V> callback,	String... symbols);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketObserver<V> callback, Instrument... instruments);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketObserver<V> callback, Exchange... exchanges);
	
	/**
	 * Fires on ALL
	 * 
	 * @param callback
	 * @param instruments
	 * @return
	 */
	Agent subscribeMarket(MarketObserver<Market> callback, String... symbols);
	
	/**
	 * Fires on TRADE
	 * 
	 * @param lastTrade
	 * @param instruments
	 * @return
	 */
	Agent subscribeTrade(MarketObserver<Trade> lastTrade, String... symbols);
	
	/**
	 * Fires on BOOK_UPDATE and BOOK_SNAPSHOT
	 * 
	 * @param book
	 * @param instruments
	 * @return
	 */
	Agent subscribeBook(MarketObserver<OrderBook> book, String... symbols);
	
	/**
	 * Fires on CUVOL_UPDATE and CUVOL_SNAPSHOT
	 * 
	 * @param cuvol
	 * @param instruments
	 * @return
	 */
	Agent subscribeCuvol(MarketObserver<Cuvol> cuvol, String... symbols);
	
}
