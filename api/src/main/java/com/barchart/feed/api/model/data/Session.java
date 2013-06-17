package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session>, SessionData {

	SessionData extended();
	
	SessionData previous();
	
	SessionData previousExtended();
	
	boolean isSettled();
	
}
