package com.ddfplus.market.impl.mock;

import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.message.MarketMessage;

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
