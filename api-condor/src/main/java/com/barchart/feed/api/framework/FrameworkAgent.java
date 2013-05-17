package com.barchart.feed.api.framework;

import com.barchart.feed.api.core.Agent;
import com.barchart.feed.api.core.Marketplace;
import com.barchart.feed.api.message.Message;
import com.barchart.missive.api.Tag;

public interface FrameworkAgent extends Agent {
	
	void bindMarketplace(Marketplace marketplace);

	<M extends FrameworkEntity<M>> MarketTag<M> callbackDataObjectTag();

	Tag<?>[] tagsToListenTo();

	// To be called by market on handle(MarketMessage)
	// Agent can internally route callback based on message if needed
	// And makes Market responsible for getting the data object to
	// pass into the callback
	<M extends FrameworkEntity<M>> void handle(MarketEntity market,
			Message message, FrameworkEntity<M> data);

	//

	@Override
	void activate();

	@Override
	void update();

	@Override
	void deactivate();

	@Override
	void dismiss();


}
