/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.values;

import com.barchart.feed.base.market.enums.MarketTradeField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** represents market trade transaction */
@NotMutable
public interface MarketTrade extends Value<MarketTrade> {

	<V extends Value<V>> V get(MarketTradeField<V> field);

}
