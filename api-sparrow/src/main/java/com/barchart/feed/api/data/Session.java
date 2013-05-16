package com.barchart.feed.api.data;

import com.barchart.feed.api.data.object.SessionObject;
import com.barchart.feed.api.data.primitive.SessionPrimitive;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData, SessionPrimitive, SessionObject {

	@Override
	Price open();

	@Override
	double openDouble();

	@Override
	Price high();

	@Override
	double highDouble();

	@Override
	Price low();

	@Override
	double lowDouble();

	@Override
	Price close();

	@Override
	double closeDouble();

	@Override
	Price settle();

	@Override
	double settleDouble();

	@Override
	Size volume();

	@Override
	long volumeLong();

	@Override
	Size interest();

	@Override
	long interestLong();

	@Override
	Time timeOpened();

	@Override
	long timeOpenedLong();

	@Override
	Time timeUpdated();

	@Override
	long timeUpdatedLong();

	@Override
	Time timeClosed();

	@Override
	long timeClosedLong();

}
