/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** stand alone market cumulative volume value */
@NotMutable
public interface MarketCuvolEntry extends Value<MarketCuvolEntry>, MarketEntry {

	/** logical or relative index of this cuvol entry in the price ladder */
	int place();

}
