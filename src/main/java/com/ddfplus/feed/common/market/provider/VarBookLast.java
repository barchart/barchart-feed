package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.ProxyTo;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBookEntry;

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
