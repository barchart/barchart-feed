package com.barchart.feed.market.provider.data;

import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.Session;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public class SessionBase extends ObjectMapSafe implements Session {

	@Override
	public Instrument instrument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time lastUpdateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price open() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double openDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Price high() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double highDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Price low() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price close() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double closeDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Price settle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Size volume() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long volumeLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Size interest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long interestLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time timeOpened() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long timeOpenedLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time timeUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long timeUpdatedLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time timeClosed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long timeClosedLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
