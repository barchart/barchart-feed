package com.barchart.feed.api.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData {

	Price open();

	double openDouble();

	Price high();

	double highDouble();

	Price low();
	
	double lowDouble();

	Price close();

	double closeDouble();

	Price settle();
	
	double settleDouble();

	Size volume();

	long volumeLong();

	Size interest();

	long interestLong();

	Time timeOpened();

	long timeOpenedLong();

	Time timeUpdated();

	long timeUpdatedLong();

	Time timeClosed();

	long timeClosedLong();

}
