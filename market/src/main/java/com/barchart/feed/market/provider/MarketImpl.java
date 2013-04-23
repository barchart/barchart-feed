package com.barchart.feed.market.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.CurrentSessionObject;
import com.barchart.feed.api.data.CuvolObject;
import com.barchart.feed.api.data.ExtendedSessionObject;
import com.barchart.feed.api.data.MarketDataObject;
import com.barchart.feed.api.data.MarketObject;
import com.barchart.feed.api.data.OrderBookObject;
import com.barchart.feed.api.data.PreviousSessionObject;
import com.barchart.feed.api.data.PriceLevelObject;
import com.barchart.feed.api.data.TopOfBookObject;
import com.barchart.feed.api.data.TradeObject;
import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.api.market.Market;
import com.barchart.feed.api.market.MarketMessage;
import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.provider.DefMarket;

public class MarketImpl implements Market {

	private DefMarket market;
	private volatile Update<MarketObject> lastUpdate;
	private volatile Snapshot<MarketObject> lastSnapshot;
	
	
	@Override
	public Instrument instrument() {
		return market.get(MarketField.INSTRUMENT);
	}

	@Override
	public DateTime lastChangeTime() {
		return market.get(MarketField.MARKET_TIME).asDateTime();
	}

	@Override
	public TradeObject lastTrade() {
		return market.get(MarketField.TRADE);
	}

	@Override
	public OrderBookObject orderBook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PriceLevelObject lastBookUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopOfBookObject topOfBook() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CuvolObject cuvol() {
		return market.get(MarketField.CUVOL);
	}

	@Override
	public CurrentSessionObject currentSession() {
		return (CurrentSessionObject) market.get(MarketField.BAR_CURRENT);
	}

	@Override
	public PreviousSessionObject extraSession() {
		return (PreviousSessionObject) market.get(MarketField.BAR_PREVIOUS);
	}

	@Override
	public ExtendedSessionObject previousSession() {
		return (ExtendedSessionObject) market.get(MarketField.BAR_CURRENT_EXT);
	}

	@Override
	public Update<MarketObject> lastUpdate() {
		return lastUpdate;
	}

	@Override
	public Snapshot<MarketObject> lastSnapshot() {
		return lastSnapshot;
	}

	@Override
	public <V extends MarketDataObject<V>> void handle(MarketMessage<V> message) {
		
	}

}
