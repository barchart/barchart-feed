package com.ddfplus.feed.common.market.provider;

import java.util.List;
import java.util.Set;

import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;
import com.ddfplus.feed.api.market.enums.MarketBarType;
import com.ddfplus.feed.api.market.enums.MarketEvent;
import com.ddfplus.feed.api.market.enums.MarketField;
import com.ddfplus.feed.api.market.enums.MarketStateEntry;
import com.ddfplus.feed.api.market.values.Market;
import com.ddfplus.feed.api.market.values.MarketBar;

@Mutable
public interface MarketDo extends Market {

	/* EVENTS */

	void fireEvents();

	void regAdd(RegTaker<?> regTaker);

	void regRemove(RegTaker<?> regTaker);

	boolean hasRegTakers();

	List<RegTaker<?>> regList();

	Set<MarketEvent> regEvents();

	/* VALUES */

	/** one time instrument initialization */
	void setInstrument(MarketInstrument symbol);

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
	void setTrade(MarketBarType type, PriceValue price, SizeValue size,
			TimeValue time);

	//

	void setState(MarketStateEntry entry, boolean isOn);

	/* RUN SAFE */

	/** run task inside exclusive market context */
	<Result, Param> Result runSafe(MarketSafeRunner<Result, Param> task,
			Param param);

	//

	MarketDoBar loadBar(MarketField<MarketBar> barField);

}
