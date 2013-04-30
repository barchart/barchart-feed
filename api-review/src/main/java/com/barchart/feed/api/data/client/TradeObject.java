package com.barchart.feed.api.data.client;

import org.joda.time.DateTime;

public interface TradeObject extends MarketDataObject {
	
	// MarketTradeType 
	public double getTradePrice();
	public long getTradeSize();
	public DateTime getTradeTime();

}
