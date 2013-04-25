package com.barchart.feed.api.fields;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.client.OrderBookObject;
import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.api.data.framework.Market;
import com.barchart.feed.api.data.framework.Trade;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.TagFactory;

//Interface or final class?????????
public final class MarketField {
	
	// Make Factory
	public static final MarketTag<Market> MARKET = (MarketTag)TagFactory.create(Market.class);
	
	public static final MarketTag<Instrument> INSTRUMENT = (MarketTag)TagFactory.create(Instrument.class);
	
	public static final MarketTag<Trade> LAST_TRADE = (MarketTag)TagFactory.create(Trade.class);
	
	//public static final MarketTag<Session> 
	
	public static final Tag<OrderBookObject> ORDER_BOOK = TagFactory.create(OrderBookObject.class); 

}
