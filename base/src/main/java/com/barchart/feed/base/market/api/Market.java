/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.common.anno.NotMutable;

/** represents complete market */
@NotMutable
public interface Market extends Value<Market>, 
		com.barchart.feed.api.model.data.Market {

	<V extends Value<V>> V get(MarketField<V> field);

}
