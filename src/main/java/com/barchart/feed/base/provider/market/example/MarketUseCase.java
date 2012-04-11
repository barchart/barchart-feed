/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.example;

import static com.barchart.feed.base.api.market.enums.MarketBarField.*;
import static com.barchart.feed.base.api.market.enums.MarketEvent.*;
import static com.barchart.feed.base.api.market.enums.MarketField.*;

import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.MarketTaker;
import com.barchart.feed.base.api.market.enums.MarketBookSide;
import com.barchart.feed.base.api.market.enums.MarketEvent;
import com.barchart.feed.base.api.market.enums.MarketField;
import com.barchart.feed.base.api.market.provider.MarketMakerProvider;
import com.barchart.feed.base.api.market.values.Market;
import com.barchart.feed.base.api.market.values.MarketBar;
import com.barchart.feed.base.api.market.values.MarketBook;
import com.barchart.feed.base.api.market.values.MarketBookEntry;
import com.barchart.feed.base.api.market.values.MarketCuvol;
import com.barchart.feed.base.api.market.values.MarketTrade;
import com.barchart.feed.base.api.message.MarketMessage;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.util.ValUtil;

abstract class MarketUseCase {

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
		public MarketInstrument[] bindInstruments() {
			return null; // some instruments
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onMarketEvent(final MarketEvent event,
				final MarketInstrument instrument, final Market market) {

			final MarketBar bar = market.get(BAR_CURRENT);

			final PriceValue priceHigh = bar.get(HIGH);
			final float high = ValUtil.asFloat(priceHigh);

			final PriceValue priceLast = bar.get(CLOSE);
			final float last = ValUtil.asFloat(priceLast);

			final float low = ValUtil.asFloat(bar.get(LOW));

			final int volume = bar.get(VOLUME).asInt();

			final MarketBook book = market.get(BOOK);
			final MarketBookEntry[] bookBids = book.entries(MarketBookSide.BID);

			final MarketCuvol cumVol = market.get(CUVOL);
			final PriceValue priveFirst = cumVol.priceFirst();
			final PriceValue priceStep = cumVol.priceStep();
			final SizeValue[] cumVolEntires = cumVol.entries();

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
			return new MarketEvent[] { MARKET_OPENED, NEW_HIGH };
		}

		@Override
		public MarketInstrument[] bindInstruments() {
			return null; // some instrument
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onMarketEvent(final MarketEvent event,
				final MarketInstrument instrument, final MarketBar bar) {

			switch (event) {
			case MARKET_OPENED:
				break;
			case NEW_HIGH:
				break;
			}

			final float high = ValUtil.asFloat(bar.get(HIGH));

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
			return new MarketEvent[] { MARKET_OPENED, NEW_TRADE };
		}

		@Override
		public MarketField<MarketTrade> bindField() {
			return TRADE;
		}

		@Override
		public MarketInstrument[] bindInstruments() {
			return null; // some valid instruments
		}

		@Override
		public void onMarketEvent(final MarketEvent event,
				final MarketInstrument instrument, final MarketTrade trade) {

			switch (event) {
			case NEW_TRADE:
				//
			}

			@SuppressWarnings("deprecation")
			final float price = ValUtil.asFloat(trade
					.get(MarketTradeField.PRICE));

		}

	};

	{

		maker.register(tradeTaker);

	}

}
