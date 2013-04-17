package com.barchart.feed.api.client.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.framework.inst.Instrument;

public interface MarketObject extends MarketDataObject {

	public Instrument getInstrument();
	public DateTime getLastChangeTime();
	public TradeObject getLastTrade();
	public OrderBookObject getOrderBook();
	public PriceLevelObject getLastBookUpdate();
	public TopOfBookObject getTopOfBook();
	public CuvolObject getCuvol();
	public MarketSessionObject getCurrentSession();
	public MarketSessionObject getExtraSession();
	public MarketSessionObject getPreviousSession();
	
}
