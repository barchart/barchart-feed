package com.barchart.feed.api.consumer;

import java.util.Map;

import rx.Observable;

import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.connection.Connection;
import com.barchart.feed.api.connection.ConnectionLifecycle;
import com.barchart.feed.api.connection.Subscription;
import com.barchart.feed.api.connection.TimestampListener;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface MarketService extends ConnectionLifecycle<MarketService>, 
		MetadataService, SubscriptionService {
	
	<V extends MarketData<V>> ConsumerAgent register(MarketObserver<V> callback,
			Class<V> clazz);

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
