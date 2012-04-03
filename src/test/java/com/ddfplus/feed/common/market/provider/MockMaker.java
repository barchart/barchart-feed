package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.market.enums.MarketBarType;
import com.ddfplus.feed.api.market.enums.MarketBookAction;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.enums.MarketBookType;
import com.ddfplus.feed.common.market.provider.DefBookEntry;
import com.ddfplus.feed.common.market.provider.MakerBase;
import com.ddfplus.feed.common.market.provider.MarketType;
import com.ddfplus.market.impl.mock.MockMessage;
import com.ddfplus.market.impl.mock.MockMessageVisitor;
import com.ddfplus.market.impl.mock.MockMsgBook;
import com.ddfplus.market.impl.mock.MockMsgTrade;

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
