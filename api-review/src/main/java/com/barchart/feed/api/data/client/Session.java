package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.Price;
import com.barchart.feed.api.data.Size;
import com.barchart.feed.api.data.Time;

public interface Session extends MarketData {

	public Price open();
	public double openDouble();
	
	public Price high();
	public double highDouble();
	
	public Price low();
	public double lowDouble();
	
	public Price close();
	public double closeDouble();
	
	public Price settle();
	public double settleDouble();
	
	public Size volume();
	public long volumeLong();

	// XXX Should open interest be included?  This is a futures concept which isn't
	// event guaranteed to exist for all futures.
	public Size openInterest();
	public long openInterestLong();
	
	public Time sessionClose();
	
}
