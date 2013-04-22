package com.barchart.feed.api.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketDataObject;

public interface MarketObject extends MarketDataObject<MarketObject>, Market {

	public Instrument getInstrument();
	public DateTime getLastChangeTime();
	public TradeObject getLastTrade();
	public OrderBookObject getOrderBook();
	public PriceLevelObject getLastBookUpdate();
	public TopOfBookObject getTopOfBook();
	public CuvolObject getCuvol();
	public CurrentSessionObject getCurrentSession();
	public PreviousSessionObject getExtraSession();
	public ExtendedSessionObject getPreviousSession();
	
}
