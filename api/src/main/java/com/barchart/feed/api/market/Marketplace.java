package com.barchart.feed.api.market;

import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.api.util.Filter;

public interface Marketplace {
	
	<V extends MarketDataObject<V>> Builder<V> builder();

	public interface Builder<V extends MarketDataObject<V>> {
		
		public Builder<V> filter(String... symbols);
		public Builder<V> filter(Instrument... instruments);
		public Builder<V> filter(Exchange... exchange);
		public Builder<V> filter(Class<MarketMessage<?>>... message);
		public Builder<V> filter(Filter<?>... filter);
		public MarketAgent build(MarketCallback<V> callback);
		
	}
	
}
