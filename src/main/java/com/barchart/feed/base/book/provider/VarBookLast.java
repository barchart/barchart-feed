/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.book.provider;

import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.util.anno.ProxyTo;
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
	public MarketBookSide side() {
		throw new UnsupportedOperationException();
	}

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
