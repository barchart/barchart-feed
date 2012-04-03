package com.ddfplus.feed.common.market.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ddfplus.feed.api.instrument.DefinitionService;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.MarketTaker;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.values.Market;
import com.ddfplus.feed.common.instrument.provider.MockService;

public class TestMakerBase {

	DefinitionService service;

	@Before
	public void setUp() throws Exception {
		service = new MockService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRegisterMarketTaker() {

		MockMaker maker = new MockMaker(MarketType.DDF);

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<Market> taker;
		boolean isAdded;

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return null;
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] {};
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return null;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return null;
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] {};
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertFalse(isAdded);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			@Override
			public void onMarketEvent(MarketEvent event,
					MarketInstrument instrument, Market value) {
			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);

	}

	@Test
	public void testRegisterMarketInstrument() {

		MockMaker maker = new MockMaker(MarketType.DDF);

		MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		//

		maker.unregister(inst);
		assertEquals(maker.marketCount(), 0);

		maker.unregister(inst);
		assertEquals(maker.marketCount(), 0);

	}

}
