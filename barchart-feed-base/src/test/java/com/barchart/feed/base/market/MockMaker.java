/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.book.enums.MarketBookType;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketFactory;
import com.barchart.feed.base.message.MockMessage;
import com.barchart.feed.base.message.MockMessageVisitor;
import com.barchart.feed.base.message.MockMsgBook;
import com.barchart.feed.base.message.MockMsgTrade;
import com.barchart.feed.base.provider.DefBookEntry;
import com.barchart.feed.base.provider.MakerBase;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public class MockMaker extends MakerBase<MockMessage> implements
		MockMessageVisitor<Void, MarketDo> {

	public MockMaker(final MarketFactory factory) {
		super(factory);
	}

	@Override
	protected void make(final MockMessage message, final MarketDo market) {
		message.accept(this, market);
	}

	@Override
	public Void visit(final MockMsgTrade message, final MarketDo market) {

		final MarketTradeType type = message.type;
		final MarketTradeSession session = message.session;
		final MarketTradeSequencing sequencing = message.sequencing;
		final PriceValue price = message.price;
		final SizeValue size = message.size;
		final TimeValue time = message.time;
		final TimeValue date = message.date;

		market.setTrade(type, session, sequencing, price, size, time, date);

		return null;

	}

	@Override
	public Void visit(final MockMsgBook message, final MarketDo market) {

		final MarketBookAction act = message.act;
		final MarketBookSide side = message.side;
		final MarketBookType type = message.type;
		final int place = message.place;
		final PriceValue price = message.price;
		final SizeValue size = message.size;
		final TimeValue time = message.time;

		final DefBookEntry entry = new DefBookEntry(act, side, type, place,
				price, size);

		market.setBookUpdate(entry, time);

		return null;

	}

}
