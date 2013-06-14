package com.barchart.feed.api.enums;

enum MarketEventType {

	UNKNOWN,
	
	TRADE, 
	SNAPSHOT, 
	// No Update?
	BOOK_UPDATE, 
	BOOK_SNAPSHOT, 
	CUVOL_UPDATE, 
	CUVOL_SNAPSHOT, 
	ALL;
	
	public static MarketEventType[] vals() {
		return new MarketEventType[]{
				TRADE, 
				SNAPSHOT, 
				BOOK_UPDATE, 
				BOOK_SNAPSHOT, 
				CUVOL_UPDATE, 
				CUVOL_SNAPSHOT, 
				ALL};
	}

}
