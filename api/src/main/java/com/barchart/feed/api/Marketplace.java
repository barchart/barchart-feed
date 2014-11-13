package com.barchart.feed.api;

import java.util.Map;

import rx.Observable;

import com.barchart.feed.api.connection.Connection;
import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.consumer.ConsumerAgent;
import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface Marketplace extends MarketService {

	/* ***** ***** Shortcut/Helper Methods ***** ***** */
	
	<V extends MarketData<V>> Agent subscribe(Class<V> clazz,
			MarketObserver<V> callback, String... symbols);

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
	Agent subscribeBook(MarketObserver<Book> book, String... symbols);

	/**
	 * Fires on CUVOL_UPDATE and CUVOL_SNAPSHOT
	 * 
	 * @param cuvol
	 * @param instruments
	 * @return
	 */
	Agent subscribeCuvol(MarketObserver<Cuvol> cuvol, String... symbols);
	
	
	 <V extends MarketData<V>> Agent newAgent(Class<V> dataType, MarketObserver<V> callback);
	
	/* ***** ***** Snapshot Provider ***** ***** */
	
	Market snapshot(Instrument instrument);
	
	Market snapshot(String symbol);
	
	/* ***** ***** MarketService ***** ***** */
	
	/**
	 * 
	 * @param callback
	 * @param clazz
	 * @return
	 */
	@Override
	<V extends MarketData<V>> ConsumerAgent register(MarketObserver<V> callback, Class<V> clazz);

	/* ***** ***** MarketSnapshotService ***** ***** */
	
	/**
	 * 
	 * @param instrument
	 * @return
	 */
	@Override
	Observable<Market> snapshot(InstrumentID instrument);
	
	/* ***** ***** ConnectionLifecycle ***** ***** */

	@Override
	void startup();

	@Override
	void shutdown();

	/**
	 * Applications which need to react to the connectivity state of the feed
	 * instantiate a FeedStateListener and bind it to the client.
	 * 
	 * @param listener
	 *            The listener to be bound.
	 */
	@Override
	void bindConnectionStateListener(Connection.Monitor listener);

	/**
	 * Applications which require time-stamp or heart-beat messages from the
	 * data server instantiate a TimestampListener and bind it to the
	 * client.
	 * 
	 * @param listener
	 */
	@Override
	void bindTimestampListener(TimestampListener listener);

	/* ***** ***** MetadataService ***** ***** */
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	@Override
	Observable<Map<InstrumentID, Instrument>> instrument(InstrumentID... ids);
	
	/**
	 * 
	 * @param symbols
	 * @return
	 */
	@Override
	Observable<Result<Instrument>> instrument(String... symbols);
	
	/**
	 * 
	 * @param ctx
	 * @param symbols
	 * @return
	 */
	@Override
	Observable<Result<Instrument>> instrument(SearchContext ctx, String... symbols);
	
	/* ***** ***** SubscriptionService ***** ***** */
	
	@Override
	Map<InstrumentID, Subscription<Instrument>> instruments();
	
	@Override
	Map<ExchangeID, Subscription<Exchange>> exchanges();
	
}
