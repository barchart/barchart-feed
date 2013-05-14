package com.barchart.feed.api.temp_2;

import com.barchart.feed.api.data.value.Price;
import com.barchart.feed.api.data.value.Size;
import com.barchart.feed.api.data.value.Time;

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
