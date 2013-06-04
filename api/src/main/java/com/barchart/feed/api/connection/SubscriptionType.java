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
	
	/*  
	 * This is a rough approx.  Probably going to be problems, given that JERQ can't 
	 * actually do some of what we're thinking.
	 */
	public static Set<SubscriptionType> mapMarketEvent(final MarketEventType event) {
		
		final Set<SubscriptionType> result = EnumSet.noneOf(SubscriptionType.class);
		
		switch(event) {
		
		default:
			result.add(UNKNOWN);
		case TRADE:
			result.add(BOOK_SNAPSHOT);
			result.add(BOOK_UPDATE);
			break;
		case SNAPSHOT:
			result.add(BOOK_SNAPSHOT);
			result.add(CUVOL_SNAPSHOT);
			result.add(QUOTE_SNAPSHOT);
			break;
		case BOOK_UPDATE:
			result.add(BOOK_UPDATE);
			break;
		case BOOK_SNAPSHOT:
			result.add(BOOK_SNAPSHOT);
			break;
		case CUVOL_UPDATE:
			// No such thing in current JERQ
			break;
		case CUVOL_SNAPSHOT:
			result.add(QUOTE_SNAPSHOT);
			break;
		case ALL:
			result.add(BOOK_SNAPSHOT);
			result.add(CUVOL_SNAPSHOT);
			result.add(QUOTE_SNAPSHOT);
			result.add(BOOK_UPDATE);
			result.add(QUOTE_UPDATE);
			break;
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
