/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.message;

import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.util.values.api.TimeValue;

public abstract class MockMessage implements MarketMessage {

	private final MarketInstrument inst;

	public MockMessage(MarketInstrument inst) {
		this.inst = inst;
	}

	@Override
	public MarketInstrument getInstrument() {
		return inst;
	}

	// @Override
	public TimeValue getTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract <Result, Param> Result accept(
			MockMessageVisitor<Result, Param> visitor, Param param);

}
