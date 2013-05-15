package com.barchart.feed.api.data.client;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Trade extends MarketData {
	
	// MarketTradeType 
	public Price getTradePrice();
	public double getTradePriceDouble();
	
	public Size getTradeSize();
	public long getTradeSizeLong();
	
	public Time getTradeTime();

}
