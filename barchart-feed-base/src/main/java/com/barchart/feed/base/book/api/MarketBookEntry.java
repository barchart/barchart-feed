/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.api;

import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.market.api.MarketEntry;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

@NotMutable
public interface MarketBookEntry extends Value<MarketBookEntry>, MarketEntry {

	/** bid vs ask */
	MarketBookSide side();

	/**
	 * logical position in the bid or ask side;
	 * 
	 * starts with {@link MarketBook#ENTRY_TOP}
	 * */
	int place();

}
