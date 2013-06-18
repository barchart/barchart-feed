/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.CuvolEntry;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueFreezer;

@NotMutable
public class DefCuvolEntry extends ValueFreezer<MarketCuvolEntry> implements
		MarketDoCuvolEntry, CuvolEntry {

	private final int place;
	private final PriceValue price;
	private final SizeValue size;

	public DefCuvolEntry(final int place, 
			final PriceValue price, final SizeValue size) {

		assert price != null;
		assert size != null;

		this.place = place;
		this.price = price;
		this.size = size;

	}

	DefCuvolEntry(final MarketBookEntry entry) {
		this(entry.place(), entry.priceValue(), entry.sizeValue());
	}

	@Override
	public boolean equals(final Object thatEntry) {
		if (thatEntry instanceof MarketCuvolEntry) {
			final MarketCuvolEntry that = (MarketCuvolEntry) thatEntry;
			return this.place() == that.place()
					&& this.priceValue().equals(that.priceValue())
					&& this.sizeValue().equals(that.sizeValue());
		}
		return false;
	}

	@Override
	public int place() {
		return place;
	}

	@Override
	public PriceValue priceValue() {
		return price;
	}

	@Override
	public SizeValue sizeValue() {
		return size;
	}

	@Override
	public final String toString() {
		return String.format("MarketEntry > %2d %22s %16s", // 55
				place(), priceValue(), sizeValue());
	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_CUVOL_ENTRY;
	}

	@Override
	public Price price() {
		return ValueConverter.price(price);
	}

	@Override
	public Size volume() {
		return ValueConverter.size(size);
	}

}
