/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.market.values;

import com.barchart.feed.base.api.market.enums.MarketBookSide;
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
