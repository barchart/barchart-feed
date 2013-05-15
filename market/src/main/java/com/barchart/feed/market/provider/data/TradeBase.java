package com.barchart.feed.market.provider.data;

import org.joda.time.DateTime;

import com.barchart.missive.core.ObjectMapSafe;

public class TradeBase extends ObjectMapSafe implements Trade {

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
	public Update lastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot lastSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketTag<Trade> tag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DateTime lastTime() {
		// TODO Auto-generated method stub
		return null;
	}

}
