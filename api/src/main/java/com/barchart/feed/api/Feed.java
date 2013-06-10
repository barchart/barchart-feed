package com.barchart.feed.api;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

import com.barchart.feed.api.connection.ConnectionLifecycle;
import com.barchart.feed.api.connection.ConnectionStateListener;
import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Exchange;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;
import com.barchart.feed.api.enums.MarketEventType;
import com.barchart.feed.api.inst.InstrumentService;

public interface Feed extends ConnectionLifecycle, InstrumentService<CharSequence>,
		AgentBuilder {

	/* ***** ***** ConnectionLifecycle ***** ***** */
	
	@Override
	void startup();
	
	@Override
	void shutdown();
	
	@Override
	void bindConnectionStateListener(ConnectionStateListener listener);
	
	/* ***** ***** InstrumentService ***** ***** */
	
	@Override
	Instrument lookup(CharSequence symbol);
	
	@Override
	Future<Instrument> lookupAsync(CharSequence symbol);
	
	@Override
	Map<CharSequence, Instrument> lookup(
			Collection<? extends CharSequence> symbols);
	
	@Override
	Map<CharSequence, Future<Instrument>> lookupAsync(
			Collection<? extends CharSequence> symbols);
	
	/* ***** ***** AgentBuilder ***** ***** */
	
	@Override
	<V extends MarketData<V>> Agent newAgent(Class<V> clazz, 
			MarketCallback<V> callback,	MarketEventType... types);
	
	/* ***** ***** Helper subscribe methods ***** ***** */
	
	
	// Agent a = feed.subscribe(new String[] { "IBM", "MSFT" }, MARKET, callback);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			String... instruments);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			Instrument... instruments);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			Exchange... instruments);
	
	/**
	 * Fires on ALL
	 * 
	 * @param callback
	 * @param instruments
	 * @return
	 */
	Agent subscribeMarket(MarketCallback<Market> callback, String... instruments);
	
	/**
	 * Fires on TRADE
	 * 
	 * @param lastTrade
	 * @param instruments
	 * @return
	 */
	Agent subscribeTrade(MarketCallback<Trade> lastTrade, String... instruments);
	
	/**
	 * Fires on BOOK_UPDATE and BOOK_SNAPSHOT
	 * 
	 * @param book
	 * @param instruments
	 * @return
	 */
	Agent subscribeBook(MarketCallback<OrderBook> book, String... instruments);
	
	/**
	 * Fires on BOOK_UPDATE and BOOK_SNAPSHOT
	 * 
	 * @param book
	 * @param instruments
	 * @return
	 */
	Agent subscribeTopOfBook(MarketCallback<TopOfBook> top, String... instruments);
	
	/**
	 * Fires on CUVOL_UPDATE and CUVOL_SNAPSHOT
	 * 
	 * @param cuvol
	 * @param instruments
	 * @return
	 */
	Agent subscribeCuvol(MarketCallback<Cuvol> cuvol, String... instruments);
	
}
