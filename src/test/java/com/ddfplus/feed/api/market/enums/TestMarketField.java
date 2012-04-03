package com.ddfplus.feed.api.market.enums;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.Value;

public class TestMarketField {

	static final Logger log = LoggerFactory.getLogger(TestMarketField.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testName() {

		assertEquals(MarketField.TRADE.name(), "TRADE");

	}

	@Test
	public void testInit1() {

		// MarketField<? extends Value<?>>[] values = MarketField.values();

		log.info("MarketField.MARKET.index() : {}",
				MarketField.MARKET.ordinal());

		assertNotNull(MarketField.MARKET.value());

	}

	@Test
	public void testInit2() {

		// MarketField<? extends Value<?>>[] values = MarketField.values();

		assertNotNull(MarketField.BAR_CURRENT.value());

	}

	@Test
	public void testToString() {

		final MarketField<? extends Value<?>>[] values = MarketField.values();

		for (MarketField<?> field : values) {
			log.info("field : {} ", field);
		}

		// for (DictEnum<?> field : DictEnum.valuesFor(MarketField.class)) {
		// log.info("dictenum : {} ", field);
		// }

		log.info("MarketField.size : {}", MarketField.size());

	}

}
