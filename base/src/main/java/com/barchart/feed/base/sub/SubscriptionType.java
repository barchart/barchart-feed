package com.barchart.feed.base.sub;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.PriceLevel;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.TopOfBook;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;

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
		} else if(clazz.equals(Book.class)) {
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
			result.add(QUOTE_UPDATE);
			result.add(QUOTE_SNAPSHOT);
		} else if(clazz.equals(Session.class)) {
			result.add(QUOTE_UPDATE);
			result.add(QUOTE_SNAPSHOT);
		} else {
			log.warn("Unknown class type {}", clazz.getName());
		}
		
		
		return result;
		
	}
	
}
