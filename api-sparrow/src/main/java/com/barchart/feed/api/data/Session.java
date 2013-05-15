package com.barchart.feed.api.data;

import com.barchart.feed.api.data.object.SessionObject;
import com.barchart.feed.api.data.primitive.SessionPrimitive;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

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

	// TODO Settle object?
	@Override
	double settleDouble();

	@Override
	Size volume();

	@Override
	long volumeLong();

	// XXX Should open interest be included? This is a futures concept which
	// isn't
	// event guaranteed to exist for all futures.
	@Override
	Size openInterest();

	@Override
	long openInterestLong();

	// TODO session close?

}
