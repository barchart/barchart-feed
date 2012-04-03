package com.ddfplus.feed.api.market;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.provider.MarketRegListener;

public interface MarketMaker {

	boolean isRegistered(MarketTaker<?> taker);

	/**
	 * add taker; do instrument registration; fires {@link MarketRegListener}
	 * events from invocation thread;
	 */
	<V extends Value<V>> boolean register(MarketTaker<V> taker);

	/**
	 * remove taker; do instrument un-registration; fires
	 * {@link MarketRegListener} events from invocation thread;
	 */
	<V extends Value<V>> boolean unregister(MarketTaker<V> taker);

	//

	int marketCount();

	boolean isRegistered(MarketInstrument instrument);

	//

	/**
	 * obtain market field value snapshot; returned values are frozen and
	 * disconnected from live market;
	 * 
	 * @return NULL_VALUE if market is not present
	 */
	<S extends MarketInstrument, V extends Value<V>> V //
	take(S instrument, MarketField<V> field);

}
