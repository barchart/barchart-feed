/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.NotMutable;

@NotMutable
public interface MarketBookTop extends Value<MarketBookTop>, Book.Top {

	/** last time top update on either side */
	TimeValue time();

	/** @return price, size, place for a side */
	MarketBookEntry side(Book.Side side);

}
