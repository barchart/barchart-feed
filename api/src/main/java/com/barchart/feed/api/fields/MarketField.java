package com.barchart.feed.api.fields;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.MarketTagFactory;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.feed.api.data.framework.OrderBook;
import com.barchart.feed.api.data.framework.Trade;
import com.barchart.missive.api.Tag;

//Interface or final class?????????
public final class MarketField {
	
	// Make Factory
	public static final MarketTag<Market> MARKET = MarketTagFactory.create(Market.class);
	
	public static final MarketTag<Instrument> INSTRUMENT = MarketTagFactory.create(Instrument.class);
	
	public static final MarketTag<Trade> LAST_TRADE = MarketTagFactory.create(Trade.class);
	
	//public static final MarketTag<Session> 
	
	public static final Tag<OrderBook> ORDER_BOOK = MarketTagFactory.create(OrderBook.class); 

}
