/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import com.barchart.feed.base.api.market.enums.MarketBarType;
import com.barchart.feed.base.api.market.enums.MarketBookAction;
import com.barchart.feed.base.api.market.enums.MarketBookSide;
import com.barchart.feed.base.api.market.enums.MarketBookType;
import com.barchart.feed.base.mock.MockMessage;
import com.barchart.feed.base.mock.MockMessageVisitor;
import com.barchart.feed.base.mock.MockMsgBook;
import com.barchart.feed.base.mock.MockMsgTrade;
import com.barchart.feed.base.provider.market.provider.DefBookEntry;
import com.barchart.feed.base.provider.market.provider.MakerBase;
import com.barchart.feed.base.provider.market.provider.MarketDo;
import com.barchart.feed.base.provider.market.provider.MarketType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public class MockMaker extends MakerBase<MockMessage> implements
		MockMessageVisitor<Void, MarketDo> {

	public MockMaker(MarketType factory) {
		super(factory);
	}

	@Override
	protected void make(final MockMessage message, final MarketDo market) {
		message.accept(this, market);
	}

	@Override
	public Void visit(final MockMsgTrade message, final MarketDo market) {

		MarketBarType type = message.type;
		PriceValue price = message.price;
		SizeValue size = message.size;
		TimeValue time = message.time;

		market.setTrade(type, price, size, time);

		return null;

	}

	@Override
	public Void visit(final MockMsgBook message, final MarketDo market) {

		MarketBookAction act = message.act;
		MarketBookSide side = message.side;
		MarketBookType type = message.type;
		int place = message.place;
		PriceValue price = message.price;
		SizeValue size = message.size;
		TimeValue time = message.time;

		DefBookEntry entry = new DefBookEntry(act, side, type, place, price, size);

		market.setBookUpdate(entry, time);

		return null;

	}

}
