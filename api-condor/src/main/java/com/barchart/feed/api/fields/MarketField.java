package com.barchart.feed.api.fields;

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.OrderBookEntity;
import com.barchart.feed.api.data.TopOfBookEntity;
import com.barchart.feed.api.data.TradeEntity;
import com.barchart.feed.api.framework.MarketEntity;
import com.barchart.feed.api.framework.MarketTag;
import com.barchart.feed.api.framework.MarketTagFactory;

//Interface or final class?????????
public final class MarketField {
	
	// Make Factory
	public static final MarketTag<MarketEntity> MARKET = MarketTagFactory.create(MarketEntity.class);
	
	public static final MarketTag<InstrumentEntity> INSTRUMENT = MarketTagFactory.create(InstrumentEntity.class);
	
	public static final MarketTag<TradeEntity> LAST_TRADE = MarketTagFactory.create(TradeEntity.class);
	
	//public static final MarketTag<Session> 
	
	public static final MarketTag<OrderBookEntity> ORDER_BOOK = MarketTagFactory.create(OrderBookEntity.class); 

	public static final MarketTag<TopOfBookEntity> TOP_OF_BOOK = MarketTagFactory.create(TopOfBookEntity.class);
	
}
