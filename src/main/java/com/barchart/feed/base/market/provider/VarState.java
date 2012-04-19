/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import com.barchart.feed.base.market.values.MarketState;
import com.barchart.util.anno.NotThreadSafe;

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
