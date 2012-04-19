/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.state.provider;

import com.barchart.feed.base.state.api.MarketState;
import com.barchart.util.anno.NotThreadSafe;

@NotThreadSafe
public class VarState extends DefState {

	@Override
	public MarketState freeze() {
		return new DefState(bitSet);
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

}
