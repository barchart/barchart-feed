/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.api.enums.MarketSide.*;

import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.util.anno.ProxyTo;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueFreezer;

@ProxyTo( { VarBook.class })
final class VarBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final VarBook book;

	VarBookTop(final VarBook book) {
		this.book = book;
	}

	@Override
	public final DefBookTop freeze() {
		return new DefBookTop(time(), side(BID), side(ASK));
	}

	@Override
	public final MarketBookEntry side(final MarketSide side) {
		return book.top(side);
	}

	@Override
	public final TimeValue time() {
		return book.time();
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

	@Override
	public Time lastUpdateTime() {
		return null; //TODO
	}

	@Override
	public TopOfBook copy() {
		return this.freeze();
	}

	@Override
	public PriceLevel bid() {
		return book.top(BID);
	}

	@Override
	public PriceLevel ask() {
		return book.top(ASK);
	}

}
