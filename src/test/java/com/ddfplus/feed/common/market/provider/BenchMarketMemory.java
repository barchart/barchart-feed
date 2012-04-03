package com.ddfplus.feed.common.market.provider;

import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;
import com.ddfplus.feed.api.instrument.DefinitionService;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.common.instrument.provider.MockService;
import com.ddfplus.feed.common.market.provider.MarketType;

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
