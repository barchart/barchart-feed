/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.base.market.enums.MarketField.BOOK;
import static com.barchart.feed.base.market.enums.MarketField.BOOK_LAST;
import static com.barchart.feed.base.market.enums.MarketField.BOOK_TOP;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL;
import static com.barchart.feed.base.market.enums.MarketField.CUVOL_LAST;
import static com.barchart.feed.base.market.enums.MarketField.MARKET;
import static com.barchart.feed.base.market.enums.MarketField.STATE;
import static com.barchart.feed.base.market.enums.MarketField.TRADE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.FrameworkAgent;
import com.barchart.feed.api.data.Cuvol;
import com.barchart.feed.api.data.Instrument;
import com.barchart.feed.api.data.MarketData;
import com.barchart.feed.api.data.OrderBook;
import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.TopOfBook;
import com.barchart.feed.api.data.Trade;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketDoCuvol;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.state.api.MarketState;
import com.barchart.feed.base.trade.api.MarketDoTrade;
import com.barchart.feed.base.trade.api.MarketTrade;
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
	
	protected final Map<Class<? extends MarketData<?>>, Set<FrameworkAgent<?>>> agentMap =
		new HashMap<Class<? extends MarketData<?>>, Set<FrameworkAgent<?>>>();
	
	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agentSet = 
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();

	// @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(VarMarket.class);

	RegCenter reg = new RegCenter(this);

	public VarMarket(final Instrument instrument) {
		
		super(instrument);

		/** set self reference */
		set(MARKET, this);
		
		agentMap.put(com.barchart.feed.api.data.Market.class, 
				new HashSet<FrameworkAgent<?>>());
		agentMap.put(Instrument.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(Trade.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(OrderBook.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(PriceLevel.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(TopOfBook.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(Cuvol.class, new HashSet<FrameworkAgent<?>>());
		agentMap.put(Session.class, new HashSet<FrameworkAgent<?>>());

	}

	/* ***** ***** Agent Lifecycle ***** ***** */
	
	@Override
	public synchronized void attachAgent(final FrameworkAgent<?> agent) {
		
		if(agentSet.containsKey(agent)) {
			updateAgent(agent);
			return;
		}
		
		if(!agent.accept(instrument())) {
			return;
		}
		
		agentSet.put(agent, new Boolean(false));
		
		agentMap.get(agent.type()).add(agent);
		
	}
	
	@Override
	public synchronized void updateAgent(final FrameworkAgent<?> agent) {
		
		if(!agentSet.containsKey(agent)) {
			attachAgent(agent);
			return;
		}
		
		if(!agent.accept(instrument())) {
			detachAgent(agent);
		}
		
	}
	
	@Override
	public synchronized void detachAgent(final FrameworkAgent<?> agent) {
		
		if(!agentSet.containsKey(agent)) {
			return;
		}
		
		agentSet.remove(agent);
		
		agentMap.get(agent.type()).remove(agent);
		
	}
	
	//

	@Override
	public final void fireEvents() {

		final RegCenter reg = this.reg;

		if (reg == null) {

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
	public final void regUpdate(final RegTaker<?> regTaker) {

		if (reg == null) {
			assert false : "unexpected";
			return;
		}

		reg.regUpdate(regTaker);

		if (reg.isEmptyRegs()) {
			reg = null;
		}

	}

	@Override
	public boolean hasRegTakers() {
		return reg != null;
	}

	protected final <T extends Value<T>> void set(final MarketField<T> field,
			final T value) {

		assert field != null;
		assert value != null;

		valueArray[field.ordinal()] = value;

	}

	/** do not set self reference on freeze */
	@Override
	public final Market freeze() {

		// log.info("### freeze!");

		final DefMarket that = new DefMarket(instrument);

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

	@Override
	public final <Result, Param> Result runSafe(
			final MarketSafeRunner<Result, Param> task, final Param param) {

		synchronized (this) {

			return task.runSafe(this, param);

		}

	}

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

			// TODO Value Converter
			final PriceValue priceStep = ValueBuilder.newPrice(
					instrument.tickSize().mantissa(), 
					instrument.tickSize().exponent());

			final VarCuvol varCuvol = new VarCuvol(instrument, priceStep);
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

			final BookLiquidityType type = instrument.liquidityType();

			final SizeValue size = LIMIT; // inst.get(BOOK_SIZE);
			
			// ValueConverter
			final PriceValue step = ValueBuilder.newPrice(
					instrument.tickSize().mantissa(), 
					instrument.tickSize().exponent());

			final VarBook varBook = new VarBook(instrument, type, size, step);
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

		//TODO Value Converter
		final PriceValue priceStep = ValueBuilder.newPrice(
				instrument.tickSize().mantissa(), 
				instrument.tickSize().exponent());

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
			return reg.getRegTakerList();
		}

	}

	@Override
	public final Set<MarketEvent> regEvents() {

		final RegCenter reg = this.reg;

		if (reg == null) {
			return EventSet.EMPTY;
		} else {
			return reg.getRegEventSet();
		}

	}

}
