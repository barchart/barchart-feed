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

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Cuvol;
import com.barchart.feed.api.model.data.MarketData;
import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketDoBook;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketDoCuvol;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.api.MarketDo;
import com.barchart.feed.base.market.api.MarketSafeRunner;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.feed.base.provider.VarMarket.Command.CType;
import com.barchart.feed.base.trade.api.MarketDoTrade;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.api.Value;
import com.barchart.feed.base.values.provider.ValueBuilder;
import com.barchart.util.common.anno.Mutable;
import com.barchart.util.common.anno.ThreadSafe;

/**
 * basic market life cycle; NO event management logic here
 */
@Mutable
@ThreadSafe(rule = "must use runSafe()")
public abstract class VarMarket extends DefMarket implements MarketDo {

	private final ConcurrentMap<FrameworkAgent<?>, Boolean> agentSet =
			new ConcurrentHashMap<FrameworkAgent<?>, Boolean>();
	
	protected final Set<FrameworkAgent<com.barchart.feed.api.model.data.Market>> marketAgents = 
			new HashSet<FrameworkAgent<com.barchart.feed.api.model.data.Market>>();
	protected final Set<FrameworkAgent<Trade>> tradeAgents = new HashSet<FrameworkAgent<Trade>>();
	protected final Set<FrameworkAgent<Book>> bookAgents = new HashSet<FrameworkAgent<Book>>();
	protected final Set<FrameworkAgent<Cuvol>> cuvolAgents = new HashSet<FrameworkAgent<Cuvol>>();
	protected final Set<FrameworkAgent<Session>> sessionAgents = new HashSet<FrameworkAgent<Session>>();
	
	public static class Command<T extends MarketData<T>> {
		
		public enum CType {
			ADD, REMOVE
		}
		
		private final CType t;
		private final FrameworkAgent<T> agent;
		
		public Command(final CType t, final FrameworkAgent<T> agent) {
			this.t = t;
			this.agent = agent;
		}
		
		public CType type() {
			return t;
		}
		
		public FrameworkAgent<T> agent() {
			return agent;
		}
	}
	
	protected final Queue<Command<com.barchart.feed.api.model.data.Market>> marketCmds =
			new ConcurrentLinkedQueue<Command<com.barchart.feed.api.model.data.Market>>();
	protected final Queue<Command<Trade>> tradeCmds = new ConcurrentLinkedQueue<Command<Trade>>();
	protected final Queue<Command<Book>> bookCmds = new ConcurrentLinkedQueue<Command<Book>>();
	protected final Queue<Command<Cuvol>> cuvolCmds = new ConcurrentLinkedQueue<Command<Cuvol>>();
	protected final Queue<Command<Session>> sessionCmds = new ConcurrentLinkedQueue<Command<Session>>();
	
	// @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(VarMarket.class);

	RegCenter reg = new RegCenter(this);

	public VarMarket(final Instrument instrument) {

		super(instrument);

		/** set self reference */
		set(MARKET, this);

	}
	
	@Override
	public void destroy() {
		agentSet.clear();
		marketAgents.clear();
		tradeAgents.clear();
		bookAgents.clear();
		cuvolAgents.clear();
		sessionAgents.clear();
		marketCmds.clear();
		tradeCmds.clear();
		bookCmds.clear();
		cuvolCmds.clear();
		sessionCmds.clear();
	}

	/* ***** ***** Agent Lifecycle ***** ***** */

	@SuppressWarnings("unchecked")
	@Override
	public void attachAgent(final FrameworkAgent<?> agent) {
		
		if(!agent.hasMatch(instrument())) {
			return;
		}
		
		agentSet.put(agent, new Boolean(false));
		
		switch(agent.agentType()) {
			case MARKET:
				marketCmds.add(new Command<com.barchart.feed.api.model.data.Market>(CType.ADD, 
						(FrameworkAgent<com.barchart.feed.api.model.data.Market>) agent));
				break;
			case BOOK:
				bookCmds.add(new Command<Book>(CType.ADD, (FrameworkAgent<Book>) agent));
				break;
			case TRADE:
				tradeCmds.add(new Command<Trade>(CType.ADD, (FrameworkAgent<Trade>) agent));
				break;
			case CUVOL:
				cuvolCmds.add(new Command<Cuvol>(CType.ADD, (FrameworkAgent<Cuvol>) agent));
				break;
			case SESSION:
				sessionCmds.add(new Command<Session>(CType.ADD, (FrameworkAgent<Session>) agent));
				break;
		}

	}

	@Override
	public void updateAgent(final FrameworkAgent<?> agent) {
		
		if(!agentSet.containsKey(agent)) {
			attachAgent(agent);
			return;
		}
		
		if(!agent.hasMatch(instrument())) {
			detachAgent(agent);
		}

	}

	@SuppressWarnings({"unchecked"})
	@Override
	public void detachAgent(final FrameworkAgent<?> agent) {

		if(!agentSet.containsKey(agent)) {
			return;
		}
		
		agentSet.remove(agent);
		
		switch(agent.agentType()) {
			case MARKET:
				marketCmds.add(new Command<com.barchart.feed.api.model.data.Market>(CType.REMOVE, 
						(FrameworkAgent<com.barchart.feed.api.model.data.Market>) agent));
				break;
			case BOOK:
				bookCmds.add(new Command<Book>(CType.REMOVE, (FrameworkAgent<Book>) agent));
				break;
			case TRADE:
				tradeCmds.add(new Command<Trade>(CType.REMOVE, (FrameworkAgent<Trade>) agent));
				break;
			case CUVOL:
				cuvolCmds.add(new Command<Cuvol>(CType.REMOVE, (FrameworkAgent<Cuvol>) agent));
				break;
			case SESSION:
				sessionCmds.add(new Command<Session>(CType.REMOVE, (FrameworkAgent<Session>) agent));
				break;
		}
		
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

		that.changeSet.addAll(changeSet);

		return that;

	}

	@Override
	public synchronized final <Result, Param> Result runSafe(
			final MarketSafeRunner<Result, Param> task, final Param param) {

		return task.runSafe(this, param);

	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

	protected final MarketDoTrade loadTrade() {

		MarketTrade trade = get(TRADE);

		if (trade.isFrozen()) {
			trade = new VarTrade(instrument);
			set(TRADE, trade);
		}
		
		return (MarketDoTrade) trade;

	}

	protected final com.barchart.feed.base.state.api.MarketState loadState() {

		com.barchart.feed.base.state.api.MarketState state = get(STATE);

		if (state.isFrozen()) {
			state = new VarState();
			set(STATE, state);
		}

		return state;

	}

	protected final MarketDoCuvol loadCuvol() {

		if(instrument.tickSize().isNull()) {
			return MarketDoCuvol.NULL;
		}

		MarketCuvol cuvol = get(CUVOL);

		if (cuvol.isFrozen()) {

			final PriceValue priceStep = ValueBuilder.newPrice(
					instrument.tickSize().mantissa(),
					instrument.tickSize().exponent());

			if(priceStep.mantissa() == 0) {
				System.out.println();
			}

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

			bar = new VarBar(instrument);

			set(barField, bar);

		}

		return (MarketDoBar) bar;

	}

	@Override
	public MarketBarType ensureBar(final TimeValue date) {

		final MarketDoBar bar = loadBar(MarketBarType.CURRENT.field);

		final TimeValue currDate = bar.get(MarketBarField.TRADE_DATE);

		if (currDate.equals(date)) {
			return MarketBarType.CURRENT;
		}

		final MarketDoBar prev = loadBar(MarketBarType.PREVIOUS.field);

		// Check for new trading session
		if (currDate.asMillisUTC() < date.asMillisUTC()) {

			// log.debug("New session: old=" + currDate + "; new=" + date);

			// Roll values to previous
			prev.copy(bar);

			// Reset current
			bar.clear();
			bar.set(MarketBarField.TRADE_DATE, date);
			// Copy last updated time from previous session for continuity
			bar.set(MarketBarField.BAR_TIME, prev.get(MarketBarField.BAR_TIME));

			// Reset extended
			loadBar(MarketBarType.CURRENT_EXT.field).copy(bar);

			// Notify change tracking
			setBar(MarketBarType.PREVIOUS, prev);
			setBar(MarketBarType.CURRENT, bar);

			return MarketBarType.CURRENT;

		}

		// Check previous bar
		final TimeValue prevDate = prev.get(MarketBarField.TRADE_DATE);

		if (prevDate.isNull()) {
			prev.set(MarketBarField.TRADE_DATE, date);
			return MarketBarType.PREVIOUS;
		} else if (prevDate.equals(date)) {
			return MarketBarType.PREVIOUS;
		}

		// No match, nothing to update
		return MarketBarType.NULL_BAR_TYPE;

	}

	protected final static SizeValue LIMIT = ValueBuilder
			.newSize(MarketBook.ENTRY_LIMIT);

	// XXX make final
	protected MarketDoBook loadBook() {

		MarketBook book = get(BOOK);

		if (book.isFrozen()) {

			Book.Type type = null;
			switch(instrument.liquidityType()) {
			default :
				type = Book.Type.NONE;
				break;
			case NONE:
				type = Book.Type.NONE;
				break;
			case DEFAULT:
				type = Book.Type.DEFAULT;
				break;
			case IMPLIED:
				type = Book.Type.IMPLIED;
				break;
			case COMBINED:
				type = Book.Type.COMBINED;
				break;
			}

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
