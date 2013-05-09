package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.temp.PriceValue;
import com.barchart.feed.api.data.temp.SizeValue;
import com.barchart.feed.api.data.temp.TimeValue;

public interface SessionObject extends MarketDataObject {

	public PriceValue open();
	public double openDouble();
	
	public PriceValue high();
	public double highDouble();
	
	public PriceValue low();
	public double lowDouble();
	
	public PriceValue close();
	public double closeDouble();
	
	public PriceValue settle();
	public double settleDouble();
	
	public SizeValue volume();
	public long volumeLong();

	// XXX Should open interest be included?  This is a futures concept which isn't
	// event guaranteed to exist for all futures.
	public SizeValue openInterest();
	public long openInterestLong();
	
	public TimeValue sessionClose();
	
}
