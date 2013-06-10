package com.barchart.feed.api.data;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session>, SessionData {

	SessionData extended();
	
	SessionData previous();
	
	SessionData previousExtended();
	
	boolean isSettled();
	
}
