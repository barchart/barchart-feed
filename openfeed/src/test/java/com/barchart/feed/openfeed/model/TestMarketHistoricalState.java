package com.barchart.feed.openfeed.model;

import org.junit.Test;
import org.openfeed.MarketEntry;
import org.openfeed.MarketHistoricalSnapshot;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.ValueFactory;

public class TestMarketHistoricalState {

	private static final ValueFactory V = ValueFactoryImpl.getInstance();

	@Test
	public void testTimestamp() throws Exception {

		final MarketHistoricalState state = new MarketHistoricalState();
		state.timestamp(0);
		System.out.println(state.timestamp());
		System.out.println(state.message().getBaseTimeStamp());

		final byte[] encoded = state.message().toByteArray();

		final MarketHistoricalSnapshot message = MarketHistoricalSnapshot.parseFrom(encoded);
		System.out.println(message.getBaseTimeStamp());

		final MarketHistoricalState decoded = new MarketHistoricalState(message);
		System.out.println(decoded.timestamp());
		System.out.println(decoded.message().getBaseTimeStamp());

	}

	@Test
	public void testNewEntry() throws Exception {

		final MarketHistoricalState state = new MarketHistoricalState();
		state.entryOrNew(MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA).price(V.newPrice(0.07));

		System.out.println(state.entry(MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA));
		System.out.println(state.entry(MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA).price());

		final byte[] encoded = state.message().toByteArray();
		final MarketHistoricalSnapshot message = MarketHistoricalSnapshot.parseFrom(encoded);
		final MarketHistoricalState decoded = new MarketHistoricalState(message);

		System.out.println(decoded.entry(MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA));
		System.out.println(decoded.entry(MarketEntry.Type.COMPUTED, MarketEntry.Descriptor.OPTION_DELTA).price());

	}

}
