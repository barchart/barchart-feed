/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.api;

import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.util.values.api.Value;

public interface MarketDoTrade extends MarketTrade {

	<V extends Value<V>> void set(final MarketTradeField<V> field, final V value);

}
