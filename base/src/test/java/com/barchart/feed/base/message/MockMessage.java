/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.message;

import com.barchart.feed.api.data.framework.Instrument;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.util.values.api.TimeValue;

public abstract class MockMessage implements MarketMessage {

	private final Instrument inst;

	public MockMessage(Instrument inst) {
		this.inst = inst;
	}

	@Override
	public Instrument getInstrument() {
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
