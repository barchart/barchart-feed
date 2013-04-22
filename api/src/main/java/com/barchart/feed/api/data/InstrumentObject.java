package com.barchart.feed.api.data;

import com.barchart.feed.api.Schedule;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.market.MarketDataObject;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.TimeInterval;

public interface InstrumentObject extends MarketDataObject<InstrumentObject> {
	
	InstrumentGUID GUID();
	String marketGUID();
	SecurityType securityType();
	BookLiquidityType liquidityType();
	BookStructureType bookStructure();
	int maxBookDepth();
	String instrumentDataVendor();
	String symbol();
	String description();
	String CFICode();
	String exchangeCode();
	MarketCurrency currency();
	double tickSize();
	double pointValue();
	Fraction displayFraction();
	TimeInterval instLifetime();
	Schedule marketHours();
	long timeZoneOffset();
	String timeZoneName();
	GuidList componentLegs();
	
}
