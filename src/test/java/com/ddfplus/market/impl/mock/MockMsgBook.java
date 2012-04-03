package com.ddfplus.market.impl.mock;

import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.enums.MarketBookAction;
import com.ddfplus.feed.api.market.enums.MarketBookSide;
import com.ddfplus.feed.api.market.enums.MarketBookType;

public class MockMsgBook extends MockMessage {

	public MockMsgBook(MarketInstrument inst) {
		super(inst);
	}

	@Override
	public <Result, Param> Result accept(
			MockMessageVisitor<Result, Param> visitor, Param param) {
		return visitor.visit(this, param);
	}

	public MarketBookAction act;

	public MarketBookSide side;

	public MarketBookType type;

	public int place;

	public PriceValue price;

	public SizeValue size;

	public TimeValue time;

}
