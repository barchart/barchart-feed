package com.barchart.feed.api.data.object;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface SessionObject {

	/**
	 * document object
	 */
	Price open();

	/**
	 * document object
	 */
	Price high();

	/**
	 * document object
	 */
	Price low();

	/**
	 * document object
	 */
	Price close();

	/**
	 * document object
	 */
	Price settle();

	/**
	 * document object
	 */
	Size volume();

	/**
	 * document object
	 */
	Size interest();

	/**
	 * document object
	 */
	Time timeOpened();

	/**
	 * document object
	 */
	Time timeUpdated();

	/**
	 * document object
	 */
	Time timeClosed();

}
