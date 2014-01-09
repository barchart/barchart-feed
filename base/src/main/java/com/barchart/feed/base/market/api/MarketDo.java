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
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.util.common.anno.Mutable;

@Mutable
public interface MarketDo extends Market, FrameworkAgentLifecycleHandler {

	/* OLD EVENTS */

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

	/**  */
	void setTrade(MarketTradeType type, MarketTradeSession session,
			MarketTradeSequencing sequencing, PriceValue price, SizeValue size,
			TimeValue time, TimeValue date);

	//

	void setState(MarketStateEntry entry, boolean isOn);
	
	void setLastPrice(LastPrice lastPrice);

	/* RUN SAFE */

	/** run task inside exclusive market context */
	public <Result, Param> Result runSafe(MarketSafeRunner<Result, Param> task,
			Param param);

	//

	MarketDoBar loadBar(MarketField<MarketBar> barField);

}
