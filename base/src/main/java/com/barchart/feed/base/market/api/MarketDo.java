/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import java.util.List;
import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.participant.FrameworkAgentLifecycleHandler;
import com.barchart.feed.base.provider.RegTaker;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.feed.base.values.api.BooleanValue;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.util.common.anno.Mutable;

@Mutable
public interface MarketDo extends Market, FrameworkAgentLifecycleHandler {

	/**
	 * Prepares Market for garbage collection, removes all references to agents
	 */
	void destroy();

	/* OLD EVENTS */

	void refresh();

	void fireEvents();

	void regAdd(RegTaker<?> regTaker);

	void regRemove(RegTaker<?> regTaker);

	void regUpdate(RegTaker<?> regTaker);

	boolean hasRegTakers();

	List<RegTaker<?>> regList();

	Set<MarketEvent> regEvents();

	void setChange(Component c);

	void clearChanges();

	/* VALUES */

	/** one time instrument initialization */
	void setInstrument(Instrument symbol);

	/**  */
	void setBookUpdate(MarketDoBookEntry entry, TimeValue time);

	/**  */
	void setBookSnapshot(MarketDoBookEntry[] entries, TimeValue time);

	/**  */
	void setCuvolUpdate(MarketDoCuvolEntry entry, TimeValue time);

	/**  */
	void setCuvolSnapshot(MarketDoCuvolEntry[] entries, TimeValue time);

	/**  */
	void setBar(MarketBarType type, MarketDoBar bar);

	/**
	 * Find the bar matching the given trade date and update its values. If any
	 * fields are null, they will be ignored instead of being nulled out (this
	 * does not include fields is isNull() == true).
	 */
	void setSnapshot(TimeValue tradeDate, PriceValue open, PriceValue high, PriceValue low, PriceValue close,
			PriceValue settle, PriceValue previousSettle, SizeValue volume, SizeValue interest, PriceValue vwap,
			BooleanValue isSettled, TimeValue barTime);

	/**  */
	void setTrade(MarketTradeType type, MarketTradeSession session, MarketTradeSequencing sequencing, PriceValue price,
			SizeValue size, TimeValue time, TimeValue date);

	//

	void setState(MarketStateEntry entry, boolean isOn);

	void fireCallbacks();

	/* RUN SAFE */

	/** run task inside exclusive market context */
	public <Result, Param> Result runSafe(MarketSafeRunner<Result, Param> task, Param param);

	//

	MarketDoBar loadBar(MarketField<MarketBar> barField);

	/**
	 * Ensure that a session/bar with the matching trading session date exists
	 * in this market. If the current session is earlier than the given trading
	 * session date, the current session will be rolled to the previous and the
	 * current session will be reset. This automatically marks the sessions as
	 * changed in the Market object.
	 *
	 * Note that if a session is rolled, the current session will have all null
	 * values, and the caller is responsible for properly initializing it.
	 *
	 * @param date
	 *            The trading session date
	 */
	MarketBarType ensureBar(TimeValue date);

	/**
	 * Saves the last DDF Message processed, used for error logging only.
	 * 
	 * @param message
	 *            DDF Message
	 */
	void setLastDDFMessage(MarketMessage message);

	MarketMessage getLastDDFMessage();
}
