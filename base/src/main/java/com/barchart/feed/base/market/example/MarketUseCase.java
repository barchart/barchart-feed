/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.example;

import static com.barchart.feed.base.bar.enums.MarketBarField.CLOSE;
import static com.barchart.feed.base.bar.enums.MarketBarField.HIGH;
import static com.barchart.feed.base.bar.enums.MarketBarField.LOW;
import static com.barchart.feed.base.bar.enums.MarketBarField.VOLUME;
import static com.barchart.feed.base.market.enums.MarketEvent.MARKET_STATUS_OPENED;
import static com.barchart.feed.base.market.enums.MarketEvent.MARKET_UPDATED;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_HIGH;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_TRADE;
import static com.barchart.feed.base.market.enums.MarketField.BAR_CURRENT;
import static com.barchart.feed.base.market.enums.MarketField.BOOK;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.market.enums.MarketField.TRADE;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketMakerProvider;
import com.barchart.feed.base.market.api.MarketMessage;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.util.ValueUtil;

abstract class MarketUseCase {

	//
	//

	// provider instance
	final MarketMakerProvider<MarketMessage> maker = null;

	{
		// new message came from transport
		final MarketMessage message = null; // say, BidAsk

		maker.make(message);// update market and fire events

	}

	//

	// runs in the same thread as make()
	final MarketTaker<Market> marketTaker = new MarketTaker<Market>() {

		@Override
		public MarketField<Market> bindField() {
			return MARKET;
		}

		@Override
		public MarketEvent[] bindEvents() {
			return new MarketEvent[] { MARKET_UPDATED };
		}

		@Override
		public Instrument[] bindInstruments() {
			return null; // some instruments
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onMarketEvent(final MarketEvent event,
				final Instrument instrument, final Market market) {

			final MarketBar bar = market.get(BAR_CURRENT);

			final PriceValue priceHigh = bar.get(HIGH);

			final float high = ValueUtil.asFloat(priceHigh);

			final PriceValue priceLast = bar.get(CLOSE);
			final float last = ValueUtil.asFloat(priceLast);

			final float low = ValueUtil.asFloat(bar.get(LOW));

			final int volume = bar.get(VOLUME).asInt();

			final MarketBook book = market.get(BOOK);
			final MarketBookEntry[] bookBids = book.entries(MarketSide.BID);

			final MarketCuvol cumVol = market.get(CUVOL);
			final PriceValue priveFirst = cumVol.priceFirst();
			final PriceValue priceStep = cumVol.priceStep();
			final SizeValue[] cumVolEntries = cumVol.entries();

		}

	};

	{

		maker.register(marketTaker);

	}

	//

	// runs in the same thread as make()
	final MarketTaker<MarketBar> currentTaker = new MarketTaker<MarketBar>() {

		@Override
		public MarketField<MarketBar> bindField() {
			return BAR_CURRENT;
		}

		@Override
		public MarketEvent[] bindEvents() {
			return new MarketEvent[] { MARKET_STATUS_OPENED, NEW_HIGH };
		}

		@Override
		public Instrument[] bindInstruments() {
			return null; // some instrument
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onMarketEvent(final MarketEvent event,
				final Instrument instrument, final MarketBar bar) {

			switch (event) {
			case MARKET_STATUS_OPENED:
				break;
			case NEW_HIGH:
				break;
			}

			final float high = ValueUtil.asFloat(bar.get(HIGH));

		}

	};

	{

		maker.register(currentTaker);

	}

	//

	// runs in the same thread as make()
	final MarketTaker<MarketTrade> tradeTaker = new MarketTaker<MarketTrade>() {

		@Override
		public MarketEvent[] bindEvents() {
			return new MarketEvent[] { MARKET_STATUS_OPENED, NEW_TRADE };
		}

		@Override
		public MarketField<MarketTrade> bindField() {
			return TRADE;
		}

		@Override
		public Instrument[] bindInstruments() {
			return null; // some valid instruments
		}

		@Override
		public void onMarketEvent(final MarketEvent event,
				final Instrument instrument, final MarketTrade trade) {

			switch (event) {
			case NEW_TRADE:
				//
			}

		}

	};

	{

		maker.register(tradeTaker);

	}

}
