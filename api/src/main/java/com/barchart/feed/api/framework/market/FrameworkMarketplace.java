package com.barchart.feed.api.framework.market;

import com.barchart.feed.api.client.marketplace.Marketplace;
import com.barchart.feed.api.commons.api.MarketMessage;

public interface FrameworkMarketplace extends Marketplace {

	void handle(MarketMessage<?> message);
	
}
