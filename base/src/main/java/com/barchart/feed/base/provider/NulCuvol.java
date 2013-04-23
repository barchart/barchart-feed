/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.data.CuvolObject;
import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.util.anno.NotMutable;
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
	public double firstPrice() {
		return Double.NaN;
	}

	@Override
	public double tickSize() {
		return 0;
	}

	@Override
	public long[] cuvols() {
		return new long[0];
	}

	@Override
	public Update<CuvolObject> lastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot<CuvolObject> lastSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

}
