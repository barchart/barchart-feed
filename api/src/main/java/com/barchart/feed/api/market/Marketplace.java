package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.util.Filter;


public interface Marketplace {

	void attachAgent(FrameworkAgent agent);
	void updateAgent(FrameworkAgent agent);
	void detachAgent(FrameworkAgent agent);
	
	// dispatch? 
	// This will be protected in impl
	<V extends MarketDataObject<V>> void handle(MarketMessage<V> message);
	
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
