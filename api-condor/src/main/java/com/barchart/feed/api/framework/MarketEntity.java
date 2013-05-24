package com.barchart.feed.api.framework;

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.api.data.Market;
import com.barchart.feed.api.data.OrderBookEntity;
import com.barchart.feed.api.data.TopOfBookEntity;
import com.barchart.feed.api.data.TradeEntity;
import com.barchart.missive.api.Tag;
import com.barchart.missive.core.TagFactory;

public interface MarketEntity extends Market, Comparable<MarketEntity>, 
		FrameworkEntity<MarketEntity> {

	// MARKET TAGS HERE
	
	InstrumentEntity instrumentEntity();
	
	void attach(FrameworkAgent agent);
	void update(FrameworkAgent agent);
	void detach(FrameworkAgent agent);
	
	public static final MarketTag<MarketEntity> MARKET = MarketTagFactory.create(MarketEntity.class);
	
	public static final MarketTag<InstrumentEntity> INSTRUMENT = MarketTagFactory.create(InstrumentEntity.class);
	
	public static final MarketTag<TradeEntity> LAST_TRADE = MarketTagFactory.create(TradeEntity.class);
	
	//public static final MarketTag<Session> 
	
	public static final MarketTag<OrderBookEntity> ORDER_BOOK = MarketTagFactory.create(OrderBookEntity.class); 

	public static final MarketTag<TopOfBookEntity> TOP_OF_BOOK = MarketTagFactory.create(TopOfBookEntity.class);
	
	public static final Tag<?>[] ALL = TagFactory.collectTop(MarketEntity.class);
}
