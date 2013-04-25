package com.barchart.feed.api.data.client;

import org.joda.time.DateTime;



public interface SessionObject extends MarketDataObject<SessionObject> {

	public double getOpen();
	public double getHigh();
	public double getLow();
	public double getClose();
	public double getSettle();
	
	public long getVolume();
	public long getOpenInterest();
	
	public DateTime getLastUpdate();
	public DateTime getSessionClose();
	
}
