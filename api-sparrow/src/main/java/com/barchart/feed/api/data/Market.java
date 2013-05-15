package com.barchart.feed.api.data;

public interface Market extends MarketData {

	public Trade lastTrade();
	public OrderBook orderBook();
	public PriceLevel lastBookUpdate();
	public TopOfBook topOfBook();
	public Cuvol cuvol();
	public CurrentSession currentSession();
	public PreviousSession extraSession();
	public ExtendedSession previousSession();
	
}
