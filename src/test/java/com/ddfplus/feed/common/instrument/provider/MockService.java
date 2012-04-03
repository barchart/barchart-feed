package com.ddfplus.feed.common.instrument.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static com.ddfplus.feed.api.instrument.enums.InstrumentField.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.barchart.util.values.api.TextValue;
import com.ddfplus.feed.api.instrument.DefinitionService;
import com.ddfplus.feed.api.instrument.enums.MarketDisplay.Fraction;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;

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

		MarketInstrument inst = map.get(id);

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
