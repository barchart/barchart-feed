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

	@Test
	public void testRegisterMarketTaker() {

		final MockMaker maker = new MockMaker(new MockMarketFactory());

		final MarketInstrument inst1 = service.lookup(newText("1"));

		maker.register(inst1);
		assertEquals(maker.marketCount(), 1);

		final MarketInstrument inst2 = service.lookup(newText("2"));

		maker.register(inst2);
		assertEquals(maker.marketCount(), 2);

		instArray = new MarketInstrument[] { inst1, inst2 };

		final MarketTaker<Market> taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				return MarketEvent.values();
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
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

		// original registration
		assertTrue(maker.register(taker));

		// following registration
		assertFalse(maker.register(taker));

		final RegTaker<?> regTaker = maker.getRegTaker(taker);

		final MarketInstrument[] regInstArary = regTaker.getInstruments();

		assertArrayEquals(instArray, regInstArary);

	}

}
