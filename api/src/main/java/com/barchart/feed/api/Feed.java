package com.barchart.feed.api;

import java.util.Collection;
import java.util.Map;

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
import com.barchart.feed.api.inst.InstrumentFuture;
import com.barchart.feed.api.inst.InstrumentFutureMap;
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
			MarketCallback<V> callback,	MarketEventType... types);
	
	/* ***** ***** Helper subscribe methods ***** ***** */
	
	
	// Agent a = feed.subscribe(new String[] { "IBM", "MSFT" }, MARKET, callback);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			String... symbols);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			Instrument... instruments);
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz, 
			MarketCallback<V> callback, MarketEventType[] types,
			Exchange... exchanges);
	
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
