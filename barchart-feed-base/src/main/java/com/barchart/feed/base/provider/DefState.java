/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.state.api.MarketState;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.collections.FastEnumSet;

@NotThreadSafe
class DefState extends FastEnumSet<MarketStateEntry> implements
		MarketState {

	private static final MarketStateEntry[] STATES = MarketStateEntry.values();

	public DefState() {
		super(STATES);
	}

	public DefState(final long bitSet) {
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
