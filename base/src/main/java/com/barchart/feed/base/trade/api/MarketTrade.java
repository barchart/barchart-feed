/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.trade.api;

import com.barchart.feed.api.data.Trade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** represents market trade transaction */
@NotMutable
public interface MarketTrade extends Value<MarketTrade>, Trade {

	<V extends Value<V>> V get(MarketTradeField<V> field);
	
	public MarketTradeType getTradeType();
	public MarketTradeSession getTradeSession();
	public MarketTradeSequencing getTradeSequencing();

}
