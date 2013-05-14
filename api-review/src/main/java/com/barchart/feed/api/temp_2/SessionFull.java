package com.barchart.feed.api.temp_2;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object
 */
public interface SessionFull {

	/**
	 * document object
	 */
	Price open();

	/**
	 * document object
	 */
	Size volume();

	/**
	 * document object
	 */
	Time timeOpened();

}
