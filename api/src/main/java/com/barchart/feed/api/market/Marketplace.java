package com.barchart.feed.api.market;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.util.Filter;

public interface Marketplace {
	
	void attachAgent(MarketAgent agent);
	void updateAgent(MarketAgent agent);
	void detachAgent(MarketAgent agent);
	
	<V extends MarketDataObject<V>> Builder<V> builder();

	public static interface Builder<V extends MarketDataObject<V>> {
		
		public Builder<V> filter(String... symbols);
		public Builder<V> filter(Instrument... instruments);
		public Builder<V> filter(Exchange... exchange);
		public Builder<V> filter(Class<MarketMessage<?>>... message);
		public Builder<V> filter(Filter<V>... filter);
		public MarketAgent build(MarketCallback<V> callback);
		
	}
	
}
