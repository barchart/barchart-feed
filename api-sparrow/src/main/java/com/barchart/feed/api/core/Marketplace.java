package com.barchart.feed.api.core;

import com.barchart.feed.api.data.Exchange;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.util.Inclusive;

/*
 * AKA MarketBase
 */
public interface Marketplace {

	<V extends MarketData> Builder<V> agentBuilder();

	interface Builder<V extends MarketData> {

		/** FIXME resolution involved, throw exception on failure? */
		Builder<V> filter(String... symbols);

		Builder<V> filter(Instrument... instruments);

		Builder<V> filter(Exchange... exchange);

		Builder<V> filter(Inclusive<?>... filter);

		<M extends MarketData> Agent build(MarketCallback<M> callback);

		<M extends MarketData> Agent build(MarketCallback<M> callback,
				InstrumentAcceptor filter);

	}

}
