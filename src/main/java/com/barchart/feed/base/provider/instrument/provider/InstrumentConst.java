/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.instrument.provider;

import com.barchart.feed.base.api.instrument.enums.MarketCurrency;
import com.barchart.feed.base.api.instrument.values.MarketCalendar;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;

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
