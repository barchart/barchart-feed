/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import org.joda.time.DateTime;

import com.barchart.feed.api.framework.inst.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** represents complete market */
@NotMutable
public interface Market extends Value<Market> {

	<V extends Value<V>> V get(MarketField<V> field);
	
	public Instrument getInstrument();
	public DateTime getLastChangeTime();
	public MarketTrade getLastTrade();
	public MarketBook getBook();
	public MarketBookEntry getLastBookUpdate();
	public MarketBookTop getTopOfBook();
	public MarketCuvol getCuvol();
	public MarketCuvolEntry getLastCuvolUpdate();
	
	/* Should each session be its own distinct type? */
	public MarketBar getCurrentSession();
	public MarketBar getExtraSession();
	public MarketBar getPreviousSession();

}
