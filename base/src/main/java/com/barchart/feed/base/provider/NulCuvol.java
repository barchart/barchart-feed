/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueConst;
import com.barchart.util.values.provider.ValueFreezer;

@NotMutable
public class NulCuvol extends ValueFreezer<MarketCuvol> implements MarketCuvol {

	@Override
	public PriceValue priceFirst() {
		return ValueConst.NULL_PRICE;
	}

	@Override
	public PriceValue priceStep() {
		return ValueConst.NULL_PRICE;
	}

	@Override
	public SizeValue[] entries() {
		return ValueConst.NULL_SIZE_ARRAY;
	}

	@Override
	public final String toString() {

		final StringBuilder text = new StringBuilder(512);

		text.append("Cumulative Volume");
		text.append("\n");
		text.append("Price Step :  ");
		text.append(priceStep());
		text.append("\n");
		text.append("Price First : ");
		text.append(priceFirst());
		text.append("\n");

		for (final SizeValue entry : entries()) {
			text.append(entry);
			text.append("\n");
		}

		return text.toString();

	}

	@Override
	public final boolean isNull() {
		return this == MarketConst.NULL_CUVOL;
	}

	@Override
	public Price firstPrice() {
		return ValueConverter.price(ValueConst.NULL_PRICE);
	}

	@Override
	public Price tickSize() {
		return ValueConverter.price(ValueConst.NULL_PRICE);
	}

	@Override
	public List<Size> cuvolList() {
		return new ArrayList<Size>(0);
	}

	@Override
	public Time lastUpdateTime() {
		return ValueConverter.time(ValueConst.NULL_TIME);
	}

	@Override
	public Cuvol copy() {
		return this.freeze();
	}

	@Override
	public Instrument instrument() {
		return Instrument.NULL_INSTRUMENT;
	}

}
