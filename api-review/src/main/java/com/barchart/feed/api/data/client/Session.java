package com.barchart.feed.api.data.client;

import com.barchart.feed.api.data.Price;
import com.barchart.feed.api.data.Size;
import com.barchart.feed.api.data.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData {

	/**
	 * document object
	 */
	Price open();

	/**
	 * document primitive
	 */
	double openDouble();

	/**
	 * document object
	 */
	Price high();

	/**
	 * document primitive
	 */
	double highDouble();

	/**
	 * document object
	 */
	Price low();

	/**
	 * document primitive
	 */
	double lowDouble();

	/**
	 * document object
	 */
	Price close();

	/**
	 * document primitive
	 */
	double closeDouble();

	/**
	 * document object
	 */
	Price settle();

	/**
	 * document primitive
	 */
	double settleDouble();

	/**
	 * document object
	 */
	Size volume();

	/**
	 * document primitive
	 */
	long volumeLong();

	/**
	 * document object
	 */
	// XXX Should open interest be included? This is a futures concept which
	// isn't
	// event guaranteed to exist for all futures.
	Size openInterest();

	/**
	 * document primitive
	 */
	long openInterestLong();

	/**
	 * document object
	 */
	Time sessionClose();

}
