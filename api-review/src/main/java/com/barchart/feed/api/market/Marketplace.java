package com.barchart.feed.api.market;

import com.barchart.feed.api.data.client.MarketDataObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.market.FrameworkElement;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.util.Filter;
import com.barchart.missive.api.Tag;

/*
 * AKA MarketBase
 */
public interface Marketplace {

	void attachAgent(FrameworkAgent<?> agent);
	void updateAgent(FrameworkAgent<?> agent);
	void detachAgent(FrameworkAgent<?> agent);
	
	void handle(Message message);
	
	<V extends FrameworkElement<V>> Builder<V> builder();
	
	public interface Builder<V extends FrameworkElement<V>> {
		
		public Builder<V> filter(String... symbols);
		public Builder<V> filter(Instrument... instruments);
		public Builder<V> filter(Exchange... exchange);
		public Builder<V> filter(Class<Message>... message);
		public Builder<V> filter(Filter<?>... filter);
		
		public <M extends MarketDataObject> Agent<M> build(
				MarketCallback<M> callback);
		
		public <M extends MarketDataObject> Agent<M> build(
				MarketCallback<M> callback, InstrumentFilter filter);
		
	}
	
	/*
	 * AKA "RegTaker"
	 */
	interface FrameworkAgent<V extends MarketDataObject> extends Agent<V> {

		void bindMarketplace(Marketplace marketplace);
		
		<M extends FrameworkElement<M>> MarketTag<M> callbackDataObjectTag();
		
		Tag<?>[] tagsToListenTo();
		
		// To be called by market on handle(MarketMessage)
		// Agent can internally route callback based on message if needed
		// And makes Market responsible for getting the data object to
		// pass into the callback
		<M extends FrameworkElement<M>> void handle(Market market, 
				Message message, FrameworkElement<M> data);
		
		void attach(Market market);
		void update(Market market);
		void detach(Market market);
		
	}
	
}
