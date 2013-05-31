package com.barchart.feed.api.data;

import com.barchart.util.value.api.Copyable;
import com.barchart.util.value.api.Time;

public interface MarketData<V extends MarketData<V>> extends Copyable<V> {

	public enum Type {
		MARKET, INSTRUMENT, LAST_TRADE, ORDER_BOOK,
		LAST_BOOK_UPDATE, TOP_OF_BOOK, CUVOL, SESSION_CURRENT,
		SESSION_CURRENT_EXT, SESSION_PREVIOUS, SESSION_PREVIOUS_EXT
	}
	
	public Time lastUpdateTime();
	
	public boolean isNull();

}
