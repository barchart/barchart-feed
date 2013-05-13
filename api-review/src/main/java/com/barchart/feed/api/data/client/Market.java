package com.barchart.feed.api.data.client;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.framework.InstrumentEntity;

public interface Market extends MarketData {

	public InstrumentEntity instrument();
	public DateTime lastChangeTime();
	public Trade lastTrade();
	public OrderBook orderBook();
	public PriceLevelObject lastBookUpdate();
	public TopOfBook topOfBook();
	public Cuvol cuvol();
	public CurrentSession currentSession();
	public PreviousSession extraSession();
	public ExtendedSession previousSession();
	
}
