/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.market.Snapshot;
import com.barchart.feed.api.market.Update;
import com.barchart.feed.api.market.data.CuvolObject;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

@NotMutable
class DefCuvol extends NulCuvol {

	private final SizeValue[] entries;

	private final PriceValue priceFirst;
	private final PriceValue priceStep;

	DefCuvol(final SizeValue[] entries, final PriceValue priceFirst,
			final PriceValue priceStep) {

		assert entries != null;
		assert priceFirst != null;
		assert priceStep != null;
		assert priceStep.mantissa() != 0;

		this.entries = entries;
		this.priceFirst = priceFirst;
		this.priceStep = priceStep;

	}

	@Override
	public PriceValue priceFirst() {
		return priceFirst;
	}

	@Override
	public PriceValue priceStep() {
		return priceStep;
	}

	@Override
	public SizeValue[] entries() {
		return entries;
	}
	
	@Override
	public double firstPrice() {
		return priceFirst.asDouble();
	}

	@Override
	public double tickSize() {
		return priceStep.asDouble();
	}

	@Override
	public long[] cuvols() {
		long[] cuvols = new long[entries.length];
		for(int i = 0; i < entries.length; i++) {
			cuvols[i] = entries[i].asLong();
		}
		return cuvols;
	}

	@Override
	public Update<CuvolObject> lastUpdate() {
		
		return null;
	}

	@Override
	public Snapshot<CuvolObject> lastSnapshot() {
		
		return null;
	}


}
