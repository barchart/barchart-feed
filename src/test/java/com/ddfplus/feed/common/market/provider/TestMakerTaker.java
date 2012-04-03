package com.ddfplus.feed.common.market.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static com.ddfplus.feed.api.market.enums.MarketBarField.*;
import static com.ddfplus.feed.api.market.enums.MarketBarType.*;
import static com.ddfplus.feed.api.market.enums.MarketField.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.SizeValue;
import com.ddfplus.feed.api.instrument.DefinitionService;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.MarketTaker;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.values.Market;
import com.ddfplus.feed.api.market.values.MarketBar;
import com.ddfplus.feed.common.instrument.provider.MockService;
import com.ddfplus.market.impl.mock.MockMsgTrade;

public class TestMakerTaker {

	DefinitionService service;

	private static final Logger log = LoggerFactory
			.getLogger(TestMakerTaker.class);

	@Before
	public void setUp() throws Exception {
		service = new MockService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTakerMarket() {

		final MockMaker maker = new MockMaker(MarketType.DDF);

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<Market> taker;
		boolean isAdded;

		final AtomicInteger count = new AtomicInteger(0);

		taker = new MarketTaker<Market>() {
			@Override
			public MarketEvent[] bindEvents() {
				// return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				return MarketEvent.values();
			}

			@Override
			public MarketField<Market> bindField() {
				return MarketField.MARKET;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			Market previous;

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market market) {

				count.incrementAndGet();

				log.info("event : {}  market : {}", event, market.hashCode());
				log.info("BAR_CURRENT = " + market.get(BAR_CURRENT).get(VOLUME));

				if (previous == null) {
					previous = market;
				}

				assertTrue(previous == market);
				previous = market;

				assertEquals(inst, instrument);

				final SizeValue volume = market.get(BAR_CURRENT).get(VOLUME);

				assertEquals(volume, newSize(10));

			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);
		assertEquals(maker.marketCount(), 1);

		//

		final MockMsgTrade msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(10);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 6);

	}

	@Test
	public void testTakerBar() {

		final MockMaker maker = new MockMaker(MarketType.DDF);

		final MarketInstrument inst;

		inst = service.lookup(newText("1"));

		maker.register(inst);
		assertEquals(maker.marketCount(), 1);

		MarketTaker<MarketBar> taker;
		boolean isAdded;

		final AtomicInteger count = new AtomicInteger(0);

		taker = new MarketTaker<MarketBar>() {
			@Override
			public MarketEvent[] bindEvents() {
				// return new MarketEvent[] { MarketEvent.MARKET_UPDATED };
				return MarketEvent.values();
			}

			@Override
			public MarketField<MarketBar> bindField() {
				return BAR_CURRENT;
			}

			@Override
			public MarketInstrument[] bindInstruments() {
				return new MarketInstrument[] { inst };
			}

			MarketBar previous;

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final MarketBar bar) {

				count.incrementAndGet();

				log.info("event : {}  bar : {}", event, bar.hashCode());

				if (previous == null) {
					previous = bar;
				}

				assertTrue(previous == bar);
				previous = bar;

				assertEquals(inst, instrument);

				final SizeValue volume = bar.get(VOLUME);

				assertEquals(volume, newSize(17));

			}
		};
		isAdded = maker.register(taker);
		assertTrue(isAdded);
		assertEquals(maker.marketCount(), 1);

		//

		final MockMsgTrade msgTrade = new MockMsgTrade(inst);
		msgTrade.type = CURRENT_NET;
		msgTrade.price = newPrice(100, 0);
		msgTrade.size = newSize(17);
		msgTrade.time = newTime(0);

		maker.make(msgTrade);

		assertEquals(count.get(), 6);

	}

}
