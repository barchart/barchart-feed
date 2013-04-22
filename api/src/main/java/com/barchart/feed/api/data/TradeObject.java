package com.barchart.feed.api.data;

import org.joda.time.DateTime;

public interface TradeObject {
	
	// MarketTradeType 
	public double getTradePrice();
	public long getTradeSize();
	public DateTime getTradeTime();

}
