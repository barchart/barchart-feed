package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBookEntry;
import com.ddfplus.feed.api.market.values.MarketBookTop;

@NotMutable
public class DefBookTop extends ValueFreezer<MarketBookTop> implements
		MarketBookTop {

	private final TimeValue time;

	private final MarketBookEntry bid;
	private final MarketBookEntry ask;

	public DefBookTop(final TimeValue time, final MarketBookEntry bid,
			final MarketBookEntry ask) {

		assert time != null;

		assert bid != null;
		assert ask != null;

		this.time = time;

		this.bid = bid;
		this.ask = ask;

	}

	@Override
	public MarketBookEntry side(final MarketBookSide side) {
		switch (side) {
		case BID:
			return bid;
		case ASK:
			return ask;
		default:
			throw new IllegalArgumentException("invalid book side");
		}
	}

	@Override
	public String toString() {

		final MarketBookEntry[] bidEntries = new MarketBookEntry[] { bid };

		final MarketBookEntry[] askEntries = new MarketBookEntry[] { ask };

		final DefBook book = new DefBook(time, bidEntries, askEntries,
				MarketConst.NULL_SIZES, MarketConst.NULL_SIZES);

		return book.toString();

	}

	@Override
	public boolean isNull() {
		return this == MarketConst.NULL_BOOK_TOP;
	}

	@Override
	public TimeValue time() {
		return time;
	}

}
