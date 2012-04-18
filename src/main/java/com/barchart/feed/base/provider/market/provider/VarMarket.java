/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider.market.provider;

import static com.barchart.feed.base.api.instrument.enums.InstrumentField.BOOK_TYPE;
import static com.barchart.feed.base.api.instrument.enums.InstrumentField.PRICE_STEP;
import static com.barchart.feed.base.api.market.enums.MarketField.BOOK;
import static com.barchart.feed.base.api.market.enums.MarketField.BOOK_LAST;
import static com.barchart.feed.base.api.market.enums.MarketField.BOOK_TOP;
import static com.barchart.feed.base.api.market.enums.MarketField.CUVOL;
import static com.barchart.feed.base.api.market.enums.MarketField.CUVOL_LAST;
import static com.barchart.feed.base.api.market.enums.MarketField.INSTRUMENT;
import static com.barchart.feed.base.api.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.api.market.enums.MarketField.STATE;
import static com.barchart.feed.base.api.market.enums.MarketField.TRADE;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.api.instrument.enums.InstrumentField;
import com.barchart.feed.base.api.instrument.values.MarketInstrument;
import com.barchart.feed.base.api.market.enums.MarketBookType;
import com.barchart.feed.base.api.market.enums.MarketEvent;
import com.barchart.feed.base.api.market.enums.MarketField;
import com.barchart.feed.base.api.market.values.Market;
import com.barchart.feed.base.api.market.values.MarketBar;
import com.barchart.feed.base.api.market.values.MarketBook;
import com.barchart.feed.base.api.market.values.MarketCuvol;
import com.barchart.feed.base.api.market.values.MarketState;
import com.barchart.feed.base.api.market.values.MarketTrade;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.ThreadSafe;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueBuilder;

/**
 * basic market life cycle; NO event management logic here
 */
@Mutable
@ThreadSafe(rule = "must use runSafe()")
public abstract class VarMarket extends DefMarket implements MarketDo {

	// @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(VarMarket.class);

	private RegCenter reg;

	VarMarket() {

		/** set self reference */
		set(MARKET, this);

	}

	//

	@Override
	public final void fireEvents() {

		final RegCenter reg = this.reg;

		if (reg == null) {

			// System.out.println("Reg is nulll");

			// there are no takers
			return;
		}

		reg.fireEvents();

		assert reg.isEmptyEvents();

	}

	protected final void eventAdd(final MarketEvent event) {

		final RegCenter reg = this.reg;

		if (reg == null) {

			// there are no takers
			return;
		}

		reg.eventsAdd(event);

	}

	@Override
	public final void regAdd(final RegTaker<?> regTaker) {

		if (reg == null) {
			reg = new RegCenter(this);
		}

		reg.regAdd(regTaker);

	}

	@Override
	public final void regRemove(final RegTaker<?> regTaker) {

		if (reg == null) {
			assert false : "unexpected";
			return;
		}

		reg.regRemove(regTaker);

		if (reg.isEmptyRegs()) {
			reg = null;
		}

	}

	@Override
	public boolean hasRegTakers() {
		return reg != null;
	}

	//

	protected final <T extends Value<T>> void set(final MarketField<T> field,
			final T value) {

		assert field != null;
		assert value != null;

		valueArray[field.ordinal()] = value;

	}

	//

	// protected static final int marketIndex = MARKET.ordinal();
	// private final static int marketItems = MarketField.size() - 1;

	/** do not set self reference on freeze */
	@Override
	public final Market freeze() {

		// log.info("### freeze!");

		final DefMarket that = new DefMarket();

		final Value<?>[] source = this.valueArray;
		final Value<?>[] target = that.valueArray;

		for (int k = 0; k < ARRAY_SIZE; k++) {

			if (k == MarketField.MARKET.ordinal()) {
				continue;
			}

			final Value<?> value = source[k];

			if (value == null) {
				continue;
			}

			target[k] = value.freeze();

		}

		// XXX keep null
		// target[marketOrdinal] = that;

		return that;

	}

	//

	/** TODO review use of volatile fields */
	// private final Lock thisGate = new ReentrantLock();
	// @Override
	// public final <Result, Param> Result runSafe(
	// final MarketSafeRunner<Result, Param> task, final Param param) {
	// thisGate.lock();
	// try {
	// return task.runSafe(this, param);
	// } finally {
	// thisGate.unlock();
	// }
	// }

	/** use synchronized for now */
	@Override
	public final <Result, Param> Result runSafe(
			final MarketSafeRunner<Result, Param> task, final Param param) {

		synchronized (this) {

			return task.runSafe(this, param);

		}

	}

	//
	@Override
	public final boolean isFrozen() {
		return false;
	}

	protected final MarketDoTrade loadTrade() {

		MarketTrade trade = get(TRADE);

		if (trade.isFrozen()) {
			trade = new VarTrade();
			set(TRADE, trade);
		}

		return (MarketDoTrade) trade;

	}

	protected final MarketState loadState() {

		MarketState state = get(STATE);

		if (state.isFrozen()) {
			state = new VarState();
			set(STATE, state);
		}

		return state;

	}

	protected final MarketDoCuvol loadCuvol() {

		MarketCuvol cuvol = get(CUVOL);

		if (cuvol.isFrozen()) {

			final MarketInstrument inst = get(INSTRUMENT);
			final PriceValue priceStep = inst.get(PRICE_STEP);

			final VarCuvol varCuvol = new VarCuvol(priceStep);
			final VarCuvolLast varCuvolLast = new VarCuvolLast(varCuvol);

			set(CUVOL, varCuvol);
			set(CUVOL_LAST, varCuvolLast);

			cuvol = varCuvol;

		}

		return (MarketDoCuvol) cuvol;

	}

	@Override
	public final MarketDoBar loadBar(final MarketField<MarketBar> barField) {

		MarketBar bar = get(barField);

		if (bar.isFrozen()) {

			bar = new VarBar();

			set(barField, bar);

		}

		return (MarketDoBar) bar;

	}

	protected final static SizeValue LIMIT = ValueBuilder
			.newSize(MarketBook.ENTRY_LIMIT);

	// XXX make final
	protected MarketDoBook loadBook() {

		MarketBook book = get(BOOK);

		if (book.isFrozen()) {

			final MarketInstrument inst = get(INSTRUMENT);

			final MarketBookType type = inst.get(BOOK_TYPE);
			final SizeValue size = LIMIT; // inst.get(BOOK_SIZE);
			final PriceValue step = inst.get(PRICE_STEP);

			final VarBook varBook = new VarBook(type, size, step);

			final VarBookLast varBookLast = new VarBookLast(varBook);
			final VarBookTop varBookTop = new VarBookTop(varBook);

			set(BOOK, varBook);
			set(BOOK_LAST, varBookLast);
			set(BOOK_TOP, varBookTop);

			book = varBook;

		}

		return (MarketDoBook) book;

	}

	protected final boolean isValidPrice(final PriceValue price) {

		final MarketInstrument inst = get(INSTRUMENT);

		final PriceValue priceStep = inst.get(InstrumentField.PRICE_STEP);

		if (!price.equalsScale(priceStep)) {
			log.error("not normalized");
			return false;
		}

		final long count = price.count(priceStep);

		final PriceValue priceTest = priceStep.mult(count);

		if (!price.equals(priceTest)) {
			log.error("does not fit step");
			return false;
		}

		return true;

	}

	//

	@Override
	public List<RegTaker<?>> regList() {

		final RegCenter reg = this.reg;

		if (reg == null) {
			return RegTakerList.EMPTY;
		} else {
			return reg.regsTakerList();
		}

	}

	@Override
	public final Set<MarketEvent> regEvents() {

		final RegCenter reg = this.reg;

		if (reg == null) {
			return EventSet.EMPTY;
		} else {
			return reg.regEventSet();
		}

	}

}
