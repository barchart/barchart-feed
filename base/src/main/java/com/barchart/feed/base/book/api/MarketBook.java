/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/*
 *
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.NotMutable;

@NotMutable
public interface MarketBook extends Value<MarketBook>, Book {

	/**
	 * starting value for place() on bid or ask side; book entry place() can be
	 * TOP ... LIMIT inclusive
	 */
	int ENTRY_TOP = 1;

	/**
	 * maximum number of entries in the bid or ask side;
	 * 
	 * total maximum book height is 64 + price steps in the bid/ask gap;
	 */
	int ENTRY_LIMIT = 32;

	//

	/**
	 * time of the last book update, any side, any place;
	 **/
	TimeValue time();

	
	//TODO REMOVE METHODS???
	/**
	 * non null entries; ordered by price; carry place information;
	 * 
	 * @param side
	 *            the book side
	 * @return array - the market entry[]; not null
	 * @throws NullPointerException
	 *             when side is null
	 * @throws IllegalArgumentException
	 *             when side is not BID nor ASK
	 */
	MarketBookEntry[] entries(Book.Side side) throws NullPointerException,
			IllegalArgumentException;

	/** top price: bid or ask; or NULL_PRICE if invalid */
	PriceValue priceTop(final Book.Side side);

	/** top size: bid or ask; or NULL_SIZE if invalid */
	SizeValue sizeTop(final Book.Side side);

	/** price-ladder spaced size array */
	SizeValue[] sizes(Book.Side side) throws NullPointerException;

	/** top ask - top bid; or NULL_PRICE if there is no gap */
	PriceValue priceGap();

}
