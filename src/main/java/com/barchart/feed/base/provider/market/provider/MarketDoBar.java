/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import com.barchart.feed.base.api.market.enums.MarketBarField;
import com.barchart.feed.base.api.market.values.MarketBar;
import com.barchart.util.values.api.Value;

public interface MarketDoBar extends MarketBar {

	<V extends Value<V>> void set(final MarketBarField<V> field, final V value);

}
