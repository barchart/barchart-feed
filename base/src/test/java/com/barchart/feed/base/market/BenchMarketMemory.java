/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.provider.MockDefinitionService;
import com.barchart.feed.base.values.api.TextValue;
import com.barchart.feed.base.values.provider.ValueBuilder;
import com.barchart.feed.inst.InstrumentService;

public class BenchMarketMemory {

	static MockMaker maker = new MockMaker(new MockMarketFactory());

	static int COUNT = 100 * 1000;

	static InstrumentService service = new MockDefinitionService();

	public static void main(final String[] args) throws Exception {

		Instrument[] inst = new Instrument[COUNT];

		MarketTaker<Market> taker;

		for (int k = 0; k < COUNT; k++) {

			final TextValue id = ValueBuilder.newText("market-" + k);

			//inst = new Instrument[] { service.lookup(id) };

			inst = new Instrument[] {service.lookup(id)};
			
			taker = new MockTaker<Market>(inst);

			maker.register(taker);

		}

		System.gc();

		Thread.sleep(1000 * 1000);

	}
}
