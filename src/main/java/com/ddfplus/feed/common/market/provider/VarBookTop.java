package com.ddfplus.feed.common.market.provider;

import static com.ddfplus.feed.api.market.enums.MarketBookSide.*;

import com.barchart.util.anno.ProxyTo;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBookEntry;
import com.ddfplus.feed.api.market.values.MarketBookTop;

@ProxyTo( { VarBook.class })
final class VarBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final VarBook book;

	VarBookTop(final VarBook book) {
		this.book = book;
	}

	@Override
	public final DefBookTop freeze() {
		return new DefBookTop(time(), side(BID), side(ASK));
	}

	@Override
	public final MarketBookEntry side(final MarketBookSide side) {
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

}
