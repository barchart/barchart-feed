/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.List;

import com.barchart.feed.api.data.Order;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.util.anno.ProxyTo;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueFreezer;

@ProxyTo( { VarBook.class })
final class VarBookLast extends ValueFreezer<MarketBookEntry> implements
		MarketBookEntry {

	private final VarBook book;

	VarBookLast(final VarBook book) {
		this.book = book;
	}

	@Override
	public final MarketBookEntry freeze() {
		return book.last();
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

	//

	@Override
	public MarketSide side() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int place() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PriceValue priceValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SizeValue sizeValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Price price() {
		return null;
	}

	@Override
	public double priceDouble() {
		return 0;
	}

	@Override
	public Size size() {
		return null;
	}

	@Override
	public long sizeLong() {
		return 0;
	}

	@Override
	public int level() {
		return 0;
	}

	@Override
	public List<Order> orderList() {
		return null;
	}

	@Override
	public Time lastUpdateTime() {
		return null;
	}

	@Override
	public PriceLevel copy() {
		return this.freeze();
	}

}
