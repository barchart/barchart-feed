package com.barchart.feed.api.market;

import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.util.Filter;

public interface Marketplace {
	
	void attachAgent(MarketAgent agent);
	void updateAgent(MarketAgent agent);
	void detachAgent(MarketAgent agent);

	public static class Builder<V extends MarketDataObject<V>> {
		
		public Builder<V> filter(String... symbols) {
			return this;
		}
		
		public Builder<V> filter(Instrument... instruments) {
			return this;
		}
		
		public Builder<V> filter(Exchange... exchange) {
			return this;
		}
		
		public Builder<V> filter(Class<MarketMessage<?>>... message) {
			return this;
		}
		
		public Builder<V> filter(Filter<V>... filter) {
			return this;
		}
		
		public MarketAgent build(MarketCallback<V> callback) {
			return null;
		}
		
	}
	
}
