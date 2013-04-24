package com.barchart.feed.api.fields;

import com.barchart.feed.api.market.data.InstrumentObject;
import com.barchart.feed.api.market.data.MarketObject;
import com.barchart.feed.api.market.data.OrderBookObject;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.TagFactory;

//Interface or final class?????????
public final class MarketField {
	
	public static final Tag<MarketObject> MARKET = TagFactory.create(MarketObject.class);
	
	public static final Tag<InstrumentObject> INSTRUMENT = TagFactory.create(InstrumentObject.class);
	
	public static final Tag<OrderBookObject> ORDER_BOOK = TagFactory.create(OrderBookObject.class); 

}
