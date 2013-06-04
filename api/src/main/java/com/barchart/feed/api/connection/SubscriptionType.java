package com.barchart.feed.api.connection;

import java.util.EnumSet;
import java.util.Set;

import com.barchart.feed.api.enums.MarketEventType;

public enum SubscriptionType {
	
	UNKNOWN,
	
	BOOK_SNAPSHOT,
	BOOK_UPDATE,
	
	CUVOL_SNAPSHOT,
	
	QUOTE_SNAPSHOT,
	QUOTE_UPDATE;
	
	public static Set<SubscriptionType> mapMarketEvent(final MarketEventType event) {
		
		final Set<SubscriptionType> result = EnumSet.noneOf(SubscriptionType.class);
		
		switch(event) {
		
		default:
			result.add(UNKNOWN);
		case TRADE:
			 
		case SNAPSHOT:
		case BOOK_UPDATE:
		case BOOK_SNAPSHOT:
		case CUVOL_UPDATE:
		case CUVOL_SNAPSHOT:
		case ALL:
		}
		
		return result;
		
	}
	
	public static Set<SubscriptionType> mapMarketEvents(final MarketEventType[] events) {
		
		final Set<SubscriptionType> result = EnumSet.noneOf(SubscriptionType.class);
		
		for(final MarketEventType t : events) {
			result.addAll(mapMarketEvent(t));
		}
		
		return result;
		
	}

}
