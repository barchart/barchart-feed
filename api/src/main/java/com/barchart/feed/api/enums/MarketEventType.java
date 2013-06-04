package com.barchart.feed.api.enums;

public enum MarketEventType {

	UNKNOWN,
	
	TRADE, 
	SNAPSHOT, 
	// No Update?
	BOOK_UPDATE, 
	BOOK_SNAPSHOT, 
	CUVOL_UPDATE, 
	CUVOL_SNAPSHOT, 
	ALL

}
