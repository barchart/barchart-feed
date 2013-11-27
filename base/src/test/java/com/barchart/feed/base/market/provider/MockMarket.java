/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/**
 * 
 */
package com.barchart.feed.base.market.provider;

import static com.barchart.feed.base.bar.enums.MarketBarField.BAR_TIME;
import static com.barchart.feed.base.bar.enums.MarketBarField.CLOSE;
import static com.barchart.feed.base.bar.enums.MarketBarField.TRADE_DATE;
import static com.barchart.feed.base.bar.enums.MarketBarField.VOLUME;
import static com.barchart.feed.base.bar.enums.MarketBarType.CURRENT;
import static com.barchart.feed.base.market.enums.MarketEvent.MARKET_UPDATED;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_ERROR;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_SNAPSHOT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_TOP;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_BOOK_UPDATE;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_CLOSE;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_CUVOL_SNAPSHOT;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_CUVOL_UPDATE;
import static com.barchart.feed.base.market.enums.MarketEvent.NEW_VOLUME;
import static com.barchart.feed.base.market.enums.MarketField.BOOK;
import static com.barchart.feed.base.market.enums.MarketField.MARKET_TIME;
import static com.barchart.feed.base.trade.enums.MarketTradeField.PRICE;
import static com.barchart.feed.base.trade.enums.MarketTradeField.SIZE;
import static com.barchart.feed.base.trade.enums.MarketTradeField.TRADE_TIME;
import static com.barchart.feed.base.trade.enums.MarketTradeField.TYPE;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.UniBookResult;
import com.barchart.feed.base.cuvol.api.MarketDoCuvol;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.feed.base.provider.VarMarket;
import com.barchart.feed.base.state.api.MarketState;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.trade.api.MarketDoTrade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;

public class MockMarket extends VarMarket {

	public MockMarket(Instrument instrument) {
		super(instrument);
	}

	@Override
	public void setInstrument(final Instrument symbol) {
		instrument = symbol;
	}

	@Override
	public void setBookUpdate(final MarketDoBookEntry entry,
			final TimeValue time) {

		assert entry != null && time != null;

		final MarketDoBook book = (MarketDoBook) get(BOOK);

		final UniBookResult result = book.setEntry(entry);

		switch (result) {
		case TOP:
			eventAdd(NEW_BOOK_TOP);
			// continue
		case NORMAL:
			eventAdd(NEW_BOOK_UPDATE);
			break;
		default:
			eventAdd(NEW_BOOK_ERROR);
			return;
		}

		book.setTime(time);
		updateMarket(time);

	}

	@Override
	public void setBookSnapshot(final MarketDoBookEntry[] entries,
			final TimeValue time) {
		assert entries != null;
		assert time != null;

		final MarketDoBook book = (MarketDoBook) get(BOOK);

		book.clear();

		for (final MarketDoBookEntry entry : entries) {

			if (entry == null) {
				continue;
			}

			final UniBookResult result = book.setEntry(entry);

			switch (result) {
			case TOP:
			case NORMAL:
				break;
			default:
				eventAdd(NEW_BOOK_ERROR);
				break;
			}

		}

		eventAdd(NEW_BOOK_SNAPSHOT);

		book.setTime(time);
		updateMarket(time);

	}

	@Override
	public void setCuvolUpdate(final MarketDoCuvolEntry entry,
			final TimeValue time) {

		assert entry != null && time != null;

		makeCuvol(entry.priceValue(), entry.sizeValue(), time);

		updateMarket(time);

	}

	@Override
	public void setCuvolSnapshot(final MarketDoCuvolEntry[] entries,
			final TimeValue time) {

		assert entries != null && time != null;

		final MarketDoCuvol cuvol = loadCuvol();

		cuvol.clear();

		for (final MarketDoCuvolEntry entry : entries) {
			cuvol.add(entry.priceValue(), entry.sizeValue(), time);
		}

		eventAdd(NEW_CUVOL_SNAPSHOT);

		updateMarket(time);

	}

	@Override
	public void setBar(final MarketBarType type, final MarketDoBar bar) {

		assert type != null;
		assert bar != null;

		set(type.field, bar);

		eventAdd(type.event);

		updateMarket(bar.get(BAR_TIME));

	}

	@Override
	public void setTrade(final MarketTradeType type,
			final MarketTradeSession session,
			final MarketTradeSequencing sequencing, final PriceValue price,
			final SizeValue size, final TimeValue time, final TimeValue date) {

		assert type != null;
		assert price != null;
		assert size != null;
		assert time != null;
		assert date != null;

		final MarketDoTrade trade = loadTrade();

		trade.set(TYPE, type);
		trade.set(PRICE, price);
		trade.set(SIZE, size);
		trade.set(TRADE_TIME, time);
		trade.set(MarketTradeField.TRADE_DATE, date);

		applyTradeToBar(CURRENT, price, size, time, date);

		makeCuvol(price, size, time);

		updateMarket(time);

	}

	private final void updateMarket(final TimeValue time) {

		set(MARKET_TIME, time);

		eventAdd(MARKET_UPDATED);

	}

	private final void makeCuvol(final PriceValue price, final SizeValue size,
			final TimeValue time) {

		final MarketDoCuvol cuvol = loadCuvol();

		cuvol.add(price, size, time);

		eventAdd(NEW_CUVOL_UPDATE);

	}

	private final void applyTradeToBar(final MarketBarType type,
			final PriceValue price, final SizeValue size, final TimeValue time,
			final TimeValue date) {

		final MarketDoBar bar = loadBar(type.field);

		eventAdd(type.event);

		final SizeValue volumeOld = bar.get(VOLUME);
		final SizeValue volumeNew = volumeOld.add(size);
		bar.set(VOLUME, volumeNew);
		eventAdd(NEW_VOLUME);

		bar.set(CLOSE, price);
		if (type == CURRENT) {
			// events only for combo
			eventAdd(NEW_CLOSE);
		}

		// ### time

		bar.set(BAR_TIME, time);
		bar.set(TRADE_DATE, date);

	}

	@Override
	public void setState(final MarketStateEntry entry, final boolean isOn) {

		assert entry != null;

		final MarketState state = loadState();

		if (isOn) {
			state.add(entry);
		} else {
			state.remove(entry);
		}

	}

	@Override
	public void setChange(Component c) {
		changeSet.add(c);
	}

	@Override
	public void clearChanges() {
		changeSet.clear();
	}

}
