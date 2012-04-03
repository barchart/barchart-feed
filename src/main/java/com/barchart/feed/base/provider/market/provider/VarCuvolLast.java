/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import com.barchart.feed.base.api.market.values.MarketCuvolEntry;
import com.barchart.util.anno.ProxyTo;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueFreezer;

@ProxyTo( { VarCuvol.class })
final class VarCuvolLast extends ValueFreezer<MarketCuvolEntry> implements
		MarketCuvolEntry {

	private final VarCuvol cuvol;

	VarCuvolLast(final VarCuvol cuvol) {
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
	public PriceValue price() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SizeValue size() {
		throw new UnsupportedOperationException();
	}

}
