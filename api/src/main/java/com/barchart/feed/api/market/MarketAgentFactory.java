package com.barchart.feed.api.market;

import com.barchart.feed.api.exchange.Exchange;
import com.barchart.feed.api.inst.Instrument;

public interface MarketAgentFactory {

	// ***** ***** No Filter/All Markets ***** *****
	
	MarketAgent forAllMarkets(MarketCallback<?> callback);
	
	MarketAgent forAllMarketsOnTrade(MarketCallback<?> callback);
	MarketAgent forAllMarketsOnBookUpdate(MarketCallback<?> callback);
	
	// ***** ***** Filter agent binding by instrument ***** *****
	
	MarketAgent forInstrument(MarketCallback<?> callback, Instrument instruments);
	MarketAgent forInstruments(MarketCallback<?> callback, Instrument[] instruments);
	
	MarketAgent forInstrumentOnTrade(MarketCallback<?> callback, Instrument instruments);
	MarketAgent forInstrumentOnBookUpdate(MarketCallback<?> callback, Instrument instruments);
	
	// ***** ***** Filter agent binding by exchange ***** *****
	
	MarketAgent forExchange(MarketCallback<?> callback, Exchange exchangeID);
	
	//...
	//...
	//...
	//...
	//...
	

}
