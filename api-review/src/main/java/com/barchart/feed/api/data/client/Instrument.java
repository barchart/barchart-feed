package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.client.primitive.InstrumentPrimitive;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.BookStructureType;
import com.barchart.feed.api.enums.MarketCurrency;
import com.barchart.feed.api.enums.SecurityType;
import com.barchart.feed.api.inst.GuidList;
import com.barchart.feed.api.inst.InstrumentGUID;
import com.barchart.feed.api.util.Schedule;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.TimeInterval;

public interface Instrument extends MarketData, InstrumentObject, 
		InstrumentPrimitive {
	
	@Override
	InstrumentGUID GUID();
	
	@Override
	String marketGUID();
	
	@Override
	SecurityType securityType();
	
	@Override
	BookLiquidityType liquidityType();
	
	@Override
	BookStructureType bookStructure();
	
	@Override
	long maxBookDepthLong();
	@Override
	Size maxBookDepth();
	
	@Override
	String instrumentDataVendor();
	
	@Override
	String symbol();
	
	@Override
	String description();
	
	@Override
	String CFICode();
	
	@Override
	String exchangeCode();
	
	@Override
	MarketCurrency currency();
	
	@Override
	double tickSizeDouble();
	@Override
	Price tickSize();
	
	@Override
	double pointValueDouble();
	@Override
	Price pointValue();
	
	@Override
	Fraction displayFraction();
	
	@Override
	TimeInterval instLifetime();
	
	@Override
	Schedule marketHours();
	
	@Override
	long timeZoneOffset();
	
	@Override
	String timeZoneName();
	
	@Override
	GuidList componentLegs();
	
}
