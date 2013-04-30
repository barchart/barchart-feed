package com.barchart.feed.market.provider.data;

import org.joda.time.DateTime;

import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.framework.Session;
import com.barchart.feed.api.message.Message;
import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;
import com.barchart.missive.core.ObjectMapSafe;

public class SessionBase extends ObjectMapSafe implements Session {

	@Override
	public double getOpen() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHigh() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getClose() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSettle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getOpenInterest() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DateTime getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getSessionClose() {
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
	public MarketTag<Session> tag() {
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
