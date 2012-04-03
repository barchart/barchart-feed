/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.instrument.provider;

import static com.barchart.feed.base.api.instrument.enums.InstrumentField.*;
import static com.barchart.util.values.provider.ValueBuilder.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.barchart.feed.base.api.instrument.DefinitionService;
import com.barchart.feed.base.api.instrument.enums.MarketDisplay.Fraction;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.provider.instrument.provider.InstrumentConst;
import com.barchart.feed.base.provider.instrument.provider.MarketDoInstrument;
import com.barchart.feed.base.provider.instrument.provider.VarInstrument;
import com.barchart.util.values.api.TextValue;

public class MockService implements DefinitionService {

	public final Map<TextValue, MarketInstrument> map = //
	new ConcurrentHashMap<TextValue, MarketInstrument>();

	public MockService() {

		MarketDoInstrument inst;
		TextValue id;

		inst = new VarInstrument();
		id = newText("1");
		inst.set(ID, id);
		inst.set(PRICE_STEP, newPrice(1, -1));
		inst.set(FRACTION, Fraction.DEC_N01);
		inst.set(BOOK_SIZE, newSize(10));
		map.put(id, inst);

		inst = new VarInstrument();
		id = newText("2");
		inst.set(ID, id);
		inst.set(PRICE_STEP, newPrice(25, -2));
		inst.set(FRACTION, Fraction.DEC_N02);
		inst.set(BOOK_SIZE, newSize(10));
		map.put(id, inst);

		inst = new VarInstrument();
		id = newText("3");
		inst.set(ID, id);
		inst.set(PRICE_STEP, newPrice(125, -3));
		inst.set(FRACTION, Fraction.BIN_N03);
		inst.set(BOOK_SIZE, newSize(10));
		map.put(id, inst);

	}

	@Override
	public MarketInstrument lookup(final TextValue id) {

		final MarketInstrument inst = map.get(id);

		if (inst == null) {
			return InstrumentConst.NULL_INSTRUMENT;
		} else {
			return inst;
		}

	}

	@Override
	public void clear() {
		map.clear();
	}

}
