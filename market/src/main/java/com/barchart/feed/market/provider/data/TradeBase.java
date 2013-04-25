package com.barchart.feed.market.provider.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.client.TradeObject;
import com.barchart.feed.api.data.framework.Trade;
import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;
import com.barchart.missive.core.ObjectMap;

public class TradeBase extends ObjectMap implements Trade {

	@Override
	public double getTradePrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTradeSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DateTime getTradeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Update<Trade> lastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot<Trade> lastSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketTag<Trade> tag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Update<Trade> update) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void snapshot(Snapshot<Trade> snapshot) {
		// TODO Auto-generated method stub
		
	}

}
