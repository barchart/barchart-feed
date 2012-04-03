package com.ddfplus.feed.common.market.provider;

import static com.ddfplus.feed.common.market.provider.MarketConst.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueConst;
import com.barchart.util.values.provider.ValueFreezer;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBook;
import com.ddfplus.feed.api.market.values.MarketBookEntry;

@NotMutable
class DefBook extends ValueFreezer<MarketBook> implements MarketBook {

	private final TimeValue time;

	private final MarketBookEntry[] bids;
	private final MarketBookEntry[] asks;

	DefBook(final TimeValue time, final MarketBookEntry[] bids,
			final MarketBookEntry[] asks, final SizeValue[] bidSizes,
			final SizeValue[] askSizes) {

		assert time != null;
		assert bids != null;
		assert asks != null;

		this.time = time;

		this.bids = bids;
		this.asks = asks;

	}

	@Override
	public MarketBookEntry[] entries(final MarketBookSide side) {
		switch (side) {
		case BID:
			return bids;
		case ASK:
			return asks;
		default:
			throw new IllegalArgumentException("invalid book side=" + side);
		}
	}

	@Override
	public final String toString() {

		final int bidDepth = bids.length;

		final int askDepth = asks.length;

		final int maxDepth = Math.max(bidDepth, askDepth);

		StringBuilder text = new StringBuilder(512);

		text.append(String.format("%55s", "BID"));
		text.append(String.format("%3s", " | "));
		text.append(String.format("%55s", "ASK"));
		text.append("\n");

		for (int k = 0; k < maxDepth; k++) {

			final int bidsOffset = maxDepth - k - 1;
			final int asksOffset = k;

			final MarketBookEntry bidEntry;
			if (k < bidDepth) {
				bidEntry = bids[bidsOffset];
			} else {
				bidEntry = NULL_BOOK_ENTRY;
			}

			final MarketBookEntry askEntry;
			if (k < askDepth) {
				askEntry = asks[asksOffset];
			} else {
				askEntry = NULL_BOOK_ENTRY;
			}

			text.append(String.format("%55s", bidEntry));
			text.append(String.format("%3s", " | "));
			text.append(String.format("%55s", askEntry));
			text.append("\n");

		}

		return text.toString();

	}

	@Override
	public boolean isNull() {
		return this == MarketConst.NULL_BOOK;
	}

	@Override
	public TimeValue time() {
		return time;
	}

	private static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

	@Override
	public PriceValue priceTop(final MarketBookSide side) {
		switch (side) {
		default:
		case BID:
			if (isValid(bids)) {
				return bids[0].price();
			} else {
				return ValueConst.NULL_PRICE;
			}
		case ASK:
			if (isValid(asks)) {
				return asks[0].price();
			} else {
				return ValueConst.NULL_PRICE;
			}
		}
	}

	@Override
	public SizeValue sizeTop(final MarketBookSide side) {
		switch (side) {
		default:
		case BID:
			if (isValid(bids)) {
				return bids[0].size();
			} else {
				return ValueConst.NULL_SIZE;
			}
		case ASK:
			if (isValid(asks)) {
				return asks[0].size();
			} else {
				return ValueConst.NULL_SIZE;
			}
		}
	}

	@Override
	public SizeValue[] sizes(final MarketBookSide side) {

		final int limit = MarketBook.ENTRY_LIMIT;

		final SizeValue[] valueArray = new SizeValue[limit];
		for (int place = 0; place < limit; place++) {
			valueArray[place] = ValueConst.NULL_SIZE;
		}

		final MarketBookEntry[] entryArray;
		switch (side) {
		default:
		case BID:
			entryArray = bids;
			break;
		case ASK:
			entryArray = asks;
			break;
		}

		if (isValid(entryArray)) {
			for (final MarketBookEntry entry : entryArray) {
				valueArray[entry.place()] = entry.size();
			}
		}

		return valueArray;

	}

	@Override
	public PriceValue priceGap() {

		final PriceValue priceBid = priceTop(MarketBookSide.BID);
		if (priceBid.isNull()) {
			return ValueConst.NULL_PRICE;
		}

		final PriceValue priceAsk = priceTop(MarketBookSide.ASK);
		if (priceAsk.isNull()) {
			return ValueConst.NULL_PRICE;
		}

		return priceAsk.sub(priceBid);

	}

}
