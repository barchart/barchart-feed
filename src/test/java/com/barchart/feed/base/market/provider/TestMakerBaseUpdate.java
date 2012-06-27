/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.feed.base.instrument.MockDefinitionService;
import com.barchart.feed.base.instrument.api.DefinitionService;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.MockMaker;
import com.barchart.feed.base.market.MockMarketFactory;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;

public class TestMakerBaseUpdate {

	private DefinitionService<MarketInstrument> service;

	@Before
	public void setUp() throws Exception {

		service = new MockDefinitionService();

	}

	@After
	public void tearDown() throws Exception {
	}

	private MarketInstrument[] instArray;

	private MarketField<?> field;

	@Test
	public void testRegisterMarketTaker() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final MarketInstrument inst1 = service.lookup(newText("1"));

		// maker.register(inst1);
		// assertEquals(maker.marketCount(), 1);

		final MarketInstrument inst2 = service.lookup(newText("2"));

		// maker.register(inst2);
		// assertEquals(maker.marketCount(), 2);

		final MarketInstrument inst3 = service.lookup(newText("3"));

		//

		final MarketTaker<?> taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return MarketEvent.values();
			}

			@SuppressWarnings("unchecked")
			@Override
			public MarketField<Market> bindField() {
				return (MarketField<Market>) field;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return instArray;
			}

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market value) {
			}
		};

		field = MarketField.BAR_CURRENT;
		instArray = new MarketInstrument[] { inst1, inst2 };

		// original registration
		assertTrue(maker.register(taker));

		final RegTaker<?> regTaker = maker.getRegTaker(taker);

		// following registration
		assertFalse(maker.register(taker));

		final MarketDo market = maker.getMarket(inst2);

		// final List<RegTaker<?>> regList = market.regList();

		//

		assertTrue(maker.isRegistered(inst1));
		assertTrue(maker.isRegistered(inst2));
		assertEquals(maker.marketCount(), 2);

		assertEquals(field, regTaker.getField());
		assertArrayEquals(instArray, regTaker.getInstruments());

		//

		field = MarketField.BAR_PREVIOUS;
		instArray = new MarketInstrument[] { inst2, inst3 };

		maker.update(taker);

		assertTrue(maker.isRegistered(inst2));
		assertTrue(maker.isRegistered(inst3));
		assertEquals(maker.marketCount(), 2);

		assertEquals(field, regTaker.getField());
		assertArrayEquals(instArray, regTaker.getInstruments());

		//

		maker.unregister(taker);
		assertEquals(maker.marketCount(), 0);

	}

}
