package com.ddfplus.feed.common.instrument.provider;

import com.ddfplus.feed.api.instrument.enums.MarketCurrency;
import com.ddfplus.feed.api.instrument.values.MarketCalendar;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;

public final class InstrumentConst {

	private InstrumentConst() {
	}

	public static final MarketInstrument NULL_INSTRUMENT = //
	new NulInstrument();

	public static final MarketCalendar NULL_CALENDAR = //
	new NulCalendar();

	public static final MarketCurrency NULL_CURRENCY = //
	MarketCurrency.NULL_CURRENCY;

}
