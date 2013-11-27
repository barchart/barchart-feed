/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.market.api.MarketEntry;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.NotMutable;

@NotMutable
public interface MarketBookEntry extends Value<MarketBookEntry>, MarketEntry, 
		Book.Entry {

	/**
	 * logical position in the bid or ask side;
	 * 
	 * starts with {@link MarketBook#ENTRY_TOP}
	 * */
	int place();

}
