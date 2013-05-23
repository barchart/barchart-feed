package com.barchart.feed.market.provider.data;

import com.barchart.feed.api.data.Trade;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public class TradeBase extends ObjectMapSafe implements Trade {

	@Override
	public Time lastUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price price() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double priceDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Size size() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long sizeLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time time() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long timeLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
