/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.message;

import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public class MockMsgTrade extends MockMessage {

	public MockMsgTrade(final InstrumentEntity inst) {
		super(inst);
	}

	@Override
	public <Result, Param> Result accept(
			final MockMessageVisitor<Result, Param> visitor, final Param param) {
		return visitor.visit(this, param);
	}

	public MarketTradeType type;

	public MarketTradeSession session;

	public MarketTradeSequencing sequencing;

	public PriceValue price;

	public SizeValue size;

	public TimeValue time;

	public TimeValue date;

}
