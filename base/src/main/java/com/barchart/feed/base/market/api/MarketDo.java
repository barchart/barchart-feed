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

import com.barchart.feed.api.data.InstrumentEntity;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.provider.RegTaker;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

@Mutable
public interface MarketDo extends Market {

	/* EVENTS */

	void fireEvents();

	void regAdd(RegTaker<?> regTaker);

	void regRemove(RegTaker<?> regTaker);

	void regUpdate(RegTaker<?> regTaker);

	boolean hasRegTakers();

	List<RegTaker<?>> regList();

	Set<MarketEvent> regEvents();

	/* VALUES */

	/** one time instrument initialization */
	void setInstrument(InstrumentEntity symbol);

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

	/**  */
	void setTrade(MarketTradeType type, MarketTradeSession session,
			MarketTradeSequencing sequencing, PriceValue price, SizeValue size,
			TimeValue time, TimeValue date);

	//

	void setState(MarketStateEntry entry, boolean isOn);

	/* RUN SAFE */

	/** run task inside exclusive market context */
	public <Result, Param> Result runSafe(MarketSafeRunner<Result, Param> task,
			Param param);

	//

	MarketDoBar loadBar(MarketField<MarketBar> barField);

}
