package com.ddfplus.feed.common.util.collections;

import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.provider.ValueConst;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.values.MarketBookEntry;

@NotThreadSafe
class RingArrayEntry {

	final RingBufferBase<MarketBookEntry> array;

	final MarketBookSide side;

	final int size;

	PriceValue priceIncrement;

	public RingArrayEntry(final MarketBookSide side, final int size) {

		this.side = side;

		this.size = size;

		this.array = null; // new RingBufferBase<MarketEntry>(size);

		this.priceIncrement = ValueConst.NULL_PRICE;

	}

	public void process(final MarketBookEntry entry) {

		if (entry.isNull()) {
			return;
		}

		MarketBookEntry top = array.get(0);

		if (top.isNull()) {
			array.set(0, entry);
			return;
		}

	}

}
