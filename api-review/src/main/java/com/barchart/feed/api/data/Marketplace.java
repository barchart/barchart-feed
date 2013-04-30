package com.barchart.feed.api.data;

import com.barchart.feed.api.data.FrameworkElement;
import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.util.Filter;


public interface Marketplace {

	void attachAgent(FrameworkAgent agent);
	void updateAgent(FrameworkAgent agent);
	void detachAgent(FrameworkAgent agent);
	
	// dispatch? 
	// This will be protected in impl
	void handle(Message message);
	
	<V extends FrameworkElement<V>> Builder<V> builder();

	public interface Builder<V extends FrameworkElement<V>> {
		
		public Builder<V> filter(String... symbols);
		public Builder<V> filter(Instrument... instruments);
		public Builder<V> filter(Exchange... exchange);
		public Builder<V> filter(Class<Message>... message);
		public Builder<V> filter(Filter<?>... filter);
		
		// Not sure here
		public <O extends MarketDataObject> MarketAgent build(MarketCallback<O> callback);
		
	}
	
}
