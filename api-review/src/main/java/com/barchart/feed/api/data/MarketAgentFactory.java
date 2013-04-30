package com.barchart.feed.api.data;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.exchange.Exchange;

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
