package com.barchart.feed.market.api;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.MarketAgent;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.data.MarketDataObject;
import com.barchart.feed.api.util.Filter;
import com.barchart.missive.api.Tag;

public interface FrameworkAgent extends Filter<Instrument>, MarketAgent {

	void bindMarketplace(FrameworkMarketplace marketplace);
	
	// Eiter need a method which returns the MarketDataObject the callback needs
	// or its Tag<M>
	<M extends MarketDataObject<M>> Tag<M> callbackDataObjectTag();
	
	Tag<?>[] tagsToListenTo();
	
	// To be called by market on handle(MarketMessage)
	// Agent can internally route callback based on message if needed
	// And makes Market responsible for getting the data object to
	// pass into the callback
	<M extends MarketDataObject<M>> void handle(Market market, 
			MarketMessage<?> message, MarketDataObject<M> data);
	
	void attach(Market market);
	void update(Market market);
	void detach(Market market);
	
}
