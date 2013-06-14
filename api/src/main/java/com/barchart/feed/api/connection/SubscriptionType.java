package com.barchart.feed.api.connection;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;

public enum SubscriptionType {
	
	UNKNOWN,
	
	BOOK_SNAPSHOT,
	BOOK_UPDATE,
	
	CUVOL_SNAPSHOT,
	
	QUOTE_SNAPSHOT, 
	QUOTE_UPDATE;

	private static final Logger log = LoggerFactory.getLogger(SubscriptionType.class);
	
	/*  
	 * This is a rough approx.  Probably going to be problems, given that JERQ can't 
	 * actually do some of what we're thinking.
	 */
	public static <V extends MarketData<V>> Set<SubscriptionType> mapMarketEvent(
			final Class<V> clazz) {
		
		final Set<SubscriptionType> result = EnumSet.noneOf(SubscriptionType.class);
		
		if(clazz.equals(Market.class)) {
			result.add(BOOK_SNAPSHOT);
			result.add(CUVOL_SNAPSHOT);
			result.add(QUOTE_SNAPSHOT);
			result.add(BOOK_UPDATE);
			result.add(QUOTE_UPDATE);
		} else if(clazz.equals(Instrument.class)) {
			// Not updating
		} else if(clazz.equals(Trade.class)) {
			result.add(QUOTE_UPDATE);
			result.add(QUOTE_SNAPSHOT);
		} else if(clazz.equals(OrderBook.class)) {
			result.add(BOOK_UPDATE);
			result.add(BOOK_SNAPSHOT);
		} else if(clazz.equals(PriceLevel.class)) {
			result.add(BOOK_UPDATE);
			result.add(BOOK_SNAPSHOT);
		} else if(clazz.equals(TopOfBook.class)) {
			result.add(BOOK_UPDATE);
			result.add(BOOK_SNAPSHOT);
		} else if(clazz.equals(Cuvol.class)) {
			result.add(CUVOL_SNAPSHOT);
		} else if(clazz.equals(Session.class)) {
			result.add(QUOTE_UPDATE);
			result.add(QUOTE_SNAPSHOT);
		} else {
			log.warn("Unknown class type {}", clazz.getName());
		}
		
		
		return result;
		
	}
	
}
