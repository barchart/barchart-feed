/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.enums;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.market.enums.MarketField;
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

		log.info("MarketField.MARKET.index() : {}",
				MarketField.MARKET.ordinal());

		assertNotNull(MarketField.MARKET.value());

	}

	@Test
	public void testInit2() {

		assertNotNull(MarketField.BAR_CURRENT.value());

	}

	@Test
	public void testToString() {

		final MarketField<? extends Value<?>>[] values = MarketField.values();

		for (final MarketField<?> field : values) {
			log.info("field : {} ", field);
		}

		log.info("MarketField.size : {}", MarketField.size());

	}

}
