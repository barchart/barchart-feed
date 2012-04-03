/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.mock;

import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.enums.MarketBarType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public class MockMsgTrade extends MockMessage {

	public MockMsgTrade(MarketInstrument inst) {
		super(inst);
	}

	@Override
	public <Result, Param> Result accept(
			MockMessageVisitor<Result, Param> visitor, Param param) {
		return visitor.visit(this, param);
	}

	public MarketBarType type;

	public PriceValue price;

	public SizeValue size;

	public TimeValue time;

}
