package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotThreadSafe;
import com.ddfplus.feed.api.market.enums.MarketStateEntry;
import com.ddfplus.feed.api.market.values.MarketState;
import com.ddfplus.feed.common.util.collections.FastEnumSet;

@NotThreadSafe
class DefState extends FastEnumSet<MarketStateEntry> implements MarketState {

	private static final MarketStateEntry[] STATES = MarketStateEntry.values();

	DefState() {
		super(STATES);
	}

	DefState(final long bitSet) {
		super(STATES, bitSet);
	}

	//

	@Override
	public MarketState freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == MarketConst.NULL_STATE;
	}

	//

}
