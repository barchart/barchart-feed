/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.provider.ValueFreezer;
import com.barchart.util.common.anno.ProxyTo;

@ProxyTo( { VarBook.class })
final class VarBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final VarBook book;

	VarBookTop(final VarBook book) {
		this.book = book;
	}

	@Override
	public final DefBookTop freeze() {
		return new DefBookTop(book.instrument, time(), side(Book.Side.BID), 
				side(Book.Side.ASK));
	}

	@Override
	public final MarketBookEntry side(final Book.Side side) {
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
	public Book.Entry bid() {
		return book.top(Book.Side.BID);
	}

	@Override
	public Book.Entry ask() {
		return book.top(Book.Side.ASK);
	}

}
