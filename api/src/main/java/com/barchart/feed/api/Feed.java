package com.barchart.feed.api;

import java.util.Collection;
import java.util.Map;

import com.barchart.feed.api.connection.ConnectionFuture;
import com.barchart.feed.api.connection.ConnectionLifecycle;
import com.barchart.feed.api.connection.ConnectionStateListener;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.inst.InstrumentFuture;
import com.barchart.feed.api.inst.InstrumentFutureMap;
import com.barchart.feed.api.inst.InstrumentService;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.OrderBook;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;

public interface Feed extends ConnectionLifecycle<Feed>, InstrumentService<CharSequence>,
		AgentBuilder {

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
	
	/* ***** ***** InstrumentService ***** ***** */
	
	/**
	 * Retrieves the instrument object denoted by symbol. The local instrument
	 * cache will be checked first. If the instrument is not stored locally, a
	 * remote call to the instrument service is made.
	 * 
	 * @return NULL_INSTRUMENT if the symbol is not resolved.
	 */
	@Override
	Instrument lookup(CharSequence symbol);
	
	@Override
	InstrumentFuture lookupAsync(CharSequence symbol);
	
	/**
	 * Retrieves a list of instrument objects denoted by symbols provided. The
	 * local instrument cache will be checked first. If any instruments are not
	 * stored locally, a remote call to the instrument service is made.
	 * 
	 * @return An empty list if no symbols can be resolved.
	 */
	@Override
	Map<CharSequence, Instrument> lookup(
			Collection<? extends CharSequence> symbols);
	
	@Override
	InstrumentFutureMap<CharSequence> lookupAsync(
			Collection<? extends CharSequence> symbols);
	
	/* ***** ***** AgentBuilder ***** ***** */
	
	@Override
	<V extends MarketData<V>> Agent newAgent(Class<V> clazz, 
			MarketCallback<V> callback);
	
	/* ***** ***** Helper subscribe methods ***** ***** */
	
	
	// Agent a = feed.subscribe(new String[] { "IBM", "MSFT" }, MARKET, callback);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback,	String... symbols);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, Instrument... instruments);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, Exchange... exchanges);
	
	/**
	 * Fires on ALL
	 * 
	 * @param callback
	 * @param instruments
	 * @return
	 */
	Agent subscribeMarket(MarketCallback<Market> callback, String... symbols);
	
	/**
	 * Fires on TRADE
	 * 
	 * @param lastTrade
	 * @param instruments
	 * @return
	 */
	Agent subscribeTrade(MarketCallback<Trade> lastTrade, String... symbols);
	
	/**
	 * Fires on BOOK_UPDATE and BOOK_SNAPSHOT
	 * 
	 * @param book
	 * @param instruments
	 * @return
	 */
	Agent subscribeBook(MarketCallback<OrderBook> book, String... symbols);
	
	/**
	 * Fires on CUVOL_UPDATE and CUVOL_SNAPSHOT
	 * 
	 * @param cuvol
	 * @param instruments
	 * @return
	 */
	Agent subscribeCuvol(MarketCallback<Cuvol> cuvol, String... symbols);
	
}
