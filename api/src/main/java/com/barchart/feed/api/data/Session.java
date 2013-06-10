package com.barchart.feed.api.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session> {

	Price open();

	Price high();

	Price low();
	
	Price close();

	Price settle();
	
	Size volume();

	Size interest();

	Time timeOpened();

	Time timeUpdated();

	Time timeClosed();

}
