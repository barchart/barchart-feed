package com.ddfplus.feed.common.market.provider;

import static com.ddfplus.feed.api.instrument.enums.InstrumentField.*;
import static com.ddfplus.feed.api.market.enums.MarketBarField.*;
import static com.ddfplus.feed.api.market.enums.MarketBarType.*;
import static com.ddfplus.feed.api.market.enums.MarketEvent.*;
import static com.ddfplus.feed.api.market.enums.MarketField.*;
import static com.ddfplus.feed.api.market.enums.MarketTradeField.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.instrument.enums.InstrumentField;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.enums.MarketBarType;
import com.ddfplus.feed.api.market.enums.MarketBookType;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.enums.MarketStateEntry;
import com.ddfplus.feed.api.market.enums.MarketTradeField;
import com.ddfplus.feed.api.market.values.MarketBook;
import com.ddfplus.feed.api.market.values.MarketState;
import com.ddfplus.feed.common.instrument.provider.InstrumentConst;

/**
 * Logic #1
 * 
 * keep here value relations and event management logic only
 **/

@Mutable
public class VarMarketDDF extends VarMarket {

	private static final Logger log = LoggerFactory
			.getLogger(VarMarketDDF.class);

	private final void updateSnapShot() {
		// set(MARKET_TIME,);

		eventAdd(MARKET_UPDATED);
	}

	private final void updateMarket(final TimeValue time) {

		set(MARKET_TIME, time);

		eventAdd(MARKET_UPDATED);

	}

	@Override
	public void setInstrument(final MarketInstrument newSymbol) {

		final MarketInstrument oldInst = get(INSTRUMENT);

		if (InstrumentConst.NULL_INSTRUMENT.equals(oldInst)) {
			set(INSTRUMENT, newSymbol);
		} else {
			throw new IllegalStateException("symbol can be set only once");
		}

	}

	@Override
	public void setBookSnapshot(final MarketDoBookEntry[] entries,
			final TimeValue time) {

		assert entries != null;
		assert time != null;

		final MarketDoBook book = loadBook();

		book.setSnapshot(entries);

		eventAdd(NEW_BOOK_SNAPSHOT);

		book.setTime(time);
		updateMarket(time);

	}

	// XXX original
	public void setBookSnapshotXXX(final MarketDoBookEntry[] entries,
			final TimeValue time) {

		assert entries != null;
		assert time != null;

		final MarketDoBook book = loadBook();

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
				log.error("result : {} entry : {}", result, entry);
				break;
			}

		}

		eventAdd(NEW_BOOK_SNAPSHOT);

		book.setTime(time);
		updateMarket(time);

	}

	@Override
	public void setBookUpdate(final MarketDoBookEntry entry,
			final TimeValue time) {

		assert entry != null && time != null;

		final MarketDoBook book = loadBook();

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
			final MarketInstrument inst = get(MarketField.INSTRUMENT);
			final TextValue id = inst.get(InstrumentField.ID);
			final TextValue comment = inst.get(InstrumentField.DESCRIPTION);
			log.error("instrument : {} : {}", id, comment);
			log.error("result : {} ; entry : {} ;", result, entry);
			return;
		}

		book.setTime(time);
		updateMarket(time);

	}

	@Override
	public void setCuvolUpdate(final MarketDoCuvolEntry entry,
			final TimeValue time) {

		assert entry != null && time != null;

		makeCuvol(entry.price(), entry.size());

		updateMarket(time);

	}

	@Override
	public void setCuvolSnapshot(final MarketDoCuvolEntry[] entries,
			final TimeValue time) {

		assert entries != null && time != null;

		final MarketDoCuvol cuvol = loadCuvol();

		cuvol.clear();

		for (final MarketDoCuvolEntry entry : entries) {
			cuvol.add(entry.price(), entry.size());
		}

		eventAdd(NEW_CUVOL_SNAPSHOT);

		updateMarket(time);

	}

	private final void applyTradeToBar(final MarketBarType type,
			final PriceValue price, final SizeValue size, final TimeValue time) {

		final MarketDoBar bar = loadBar(type.field);

		// XXX this is disabled to force compatibility with ddf2
		// if (bar.get(BAR_TIME).compareTo(time) > 0) {
		// log.error("ignoring past trade");
		// return;
		// }

		eventAdd(type.event);

		// ### volume

		final SizeValue volumeOld = bar.get(VOLUME);
		final SizeValue volumeNew = volumeOld.add(size);
		bar.set(VOLUME, volumeNew);
		eventAdd(NEW_VOLUME);

		// log.debug("####### type=" + type + " old=" + volumeOld + " new="
		// + volumeNew);

		// ### high

		// XXX disable for dd2 compatibility
		// final PriceValue high = bar.get(HIGH);
		// if (price.compareTo(high) > 0 || high.isNull()) {
		// bar.set(HIGH, price);
		// if (type == CURRENT) {
		// // events only for combo
		// eventAdd(NEW_HIGH);
		// }
		// }

		// ### low

		// XXX disable for dd2 compatibility
		// final PriceValue low = bar.get(LOW);
		// if (price.compareTo(low) < 0 || low.isNull()) {
		// bar.set(LOW, price);
		// if (type == CURRENT) {
		// // events only for combo
		// eventAdd(NEW_LOW);
		// }
		// }

		// ### last

		bar.set(CLOSE, price);
		if (type == CURRENT) {
			// events only for combo
			eventAdd(NEW_CLOSE);
		}

		// ### time

		bar.set(BAR_TIME, time);

	}

	private final void makeCuvol(final PriceValue price, final SizeValue size) {

		final MarketDoCuvol cuvol = loadCuvol();

		cuvol.add(price, size);

		eventAdd(NEW_CUVOL_UPDATE);

	}

	@Override
	public void setTrade(final MarketBarType type, final PriceValue price,
			final SizeValue size, final TimeValue time) {

		assert type != null;
		assert price != null;
		assert size != null;
		assert time != null;

		// assert isValidPrice(price);

		// ### trade

		final MarketDoTrade trade = loadTrade();

		// XXX disabled to match ddf
		// if (trade.get(TRADE_TIME).compareTo(time) > 0) {
		// log.error("ignoring past trade");
		// return;
		// }

		trade.set(MarketTradeField.TYPE, type);
		trade.set(PRICE, price);
		trade.set(SIZE, size);
		trade.set(TRADE_TIME, time);

		// switch (type) {
		// case CURRENT_NET:
		// eventAdd(NEW_TRADE_NET);
		// break;
		// case CURRENT_PIT:
		// eventAdd(NEW_TRADE_PIT);
		// break;
		// default:
		// // must subscribe to combo event
		// break;
		// }

		// always combo
		eventAdd(NEW_TRADE);

		// ### bar

		// switch (type) {
		// case CURRENT_NET:
		// case CURRENT_PIT:
		// makeBar(type, price, size, time);
		// break;
		// default:
		// // ignore unknown volume sources
		// break;
		// }

		// always combo
		applyTradeToBar(CURRENT, price, size, time);

		// ### cuvol

		// always combo
		makeCuvol(price, size);

		// ### time

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

	//

	/** XXX eliminate: temp hack for ddf */
	@Override
	protected MarketDoBook loadBook() {

		MarketBook book = get(BOOK);

		if (book.isFrozen()) {

			final MarketInstrument inst = get(INSTRUMENT);

			final MarketBookType type = inst.get(BOOK_TYPE);
			final SizeValue size = LIMIT;
			final PriceValue step = inst.get(PRICE_STEP);

			final VarBookDDF varBook = new VarBookDDF(type, size, step);
			final VarBookTopDDF varBookTop = new VarBookTopDDF(varBook);
			// final VarBookLast varBookLast = new VarBookLast(varBook);

			set(BOOK, varBook);
			set(BOOK_TOP, varBookTop);
			// set(BOOK_LAST, varBookLast);

			book = varBook;

		}

		return (MarketDoBook) book;

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

}
