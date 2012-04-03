/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import com.barchart.feed.base.api.instrument.DefinitionService;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.provider.instrument.provider.MockService;
import com.barchart.feed.base.provider.market.provider.MarketType;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;

public class BenchMarketMemory {

	static MockMaker maker = new MockMaker(MarketType.DDF);

	static int COUNT = 100 * 1000;

	static DefinitionService service = new MockService();

	public static void main(String[] args) throws Exception {

		MarketInstrument inst;

		for (int k = 0; k < COUNT; k++) {

			TextValue id = ValueBuilder.newText("market-" + k);

			inst = service.lookup(id);

			maker.register(inst);

		}

		System.gc();

		Thread.sleep(1000 * 1000);

	}
}
