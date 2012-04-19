/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.instrument.MockService;
import com.barchart.feed.base.instrument.api.DefinitionService;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.values.Market;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;

public class BenchMarketMemory {

	static MockMaker maker = new MockMaker(new MockMarketFactory());

	static int COUNT = 100 * 1000;

	static DefinitionService service = new MockService();

	public static void main(final String[] args) throws Exception {

		MarketInstrument[] inst = new MarketInstrument[COUNT];

		MarketTaker<Market> taker;

		for (int k = 0; k < COUNT; k++) {

			final TextValue id = ValueBuilder.newText("market-" + k);

			inst = new MarketInstrument[] { service.lookup(id) };

			taker = new MockTaker<Market>(inst);

			maker.register(taker);

		}

		System.gc();

		Thread.sleep(1000 * 1000);

	}
}
