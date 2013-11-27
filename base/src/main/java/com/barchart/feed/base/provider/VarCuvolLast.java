/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.anno.ProxyTo;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

@ProxyTo({ VarCuvol.class })
public final class VarCuvolLast extends ValueFreezer<MarketCuvolEntry>
		implements MarketCuvolEntry {

	private final VarCuvol cuvol;

	public VarCuvolLast(final VarCuvol cuvol) {
		this.cuvol = cuvol;
	}

	@Override
	public final MarketCuvolEntry freeze() {
		return cuvol.getLastEntry();
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

	//

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
		throw new UnsupportedOperationException();
	}

	@Override
	public Size size() {
		throw new UnsupportedOperationException();
	}

}
