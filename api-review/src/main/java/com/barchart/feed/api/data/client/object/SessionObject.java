package com.barchart.feed.api.data.client.object;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

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
	// XXX Should open interest be included? This is a futures concept which
	// isn't
	// event guaranteed to exist for all futures.
	Size openInterest();
	
}
