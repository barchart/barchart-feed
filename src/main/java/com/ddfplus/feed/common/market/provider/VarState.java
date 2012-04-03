package com.ddfplus.feed.common.market.provider;

import com.barchart.util.anno.NotThreadSafe;
import com.ddfplus.feed.api.market.values.MarketState;

@NotThreadSafe
class VarState extends DefState {

	@Override
	public MarketState freeze() {
		return new DefState(bitSet);
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

}
