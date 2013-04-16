package com.barchart.feed.base.market.api;

import java.util.List;
import java.util.Set;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

public interface NEW_MarketDo extends Market {
	
	/* EVENTS */
	
	void fireEvents();
	void addAgent(NEWMarketAgent<?,?> agent);
	void removeAgent(NEWMarketAgent<?,?> agent);
	
	List<NEWMarketAgent<?,?>> agents();
	Set<MarketEvent> events();
	
	/* VALUES */
	
	void setBookUpdate(MarketDoBookEntry entry, TimeValue time);
	void setBookSnapshot(MarketDoBookEntry[] entries, TimeValue time);
	void setCuvolUpdate(MarketDoCuvolEntry entry, TimeValue time);
	void setCuvolSnapshot(MarketDoCuvolEntry[] entries, TimeValue time);
	void setBar(MarketBarType type, MarketDoBar bar);
	void setTrade(MarketTradeType type, MarketTradeSession session,
			MarketTradeSequencing sequencing, PriceValue price, SizeValue size,
			TimeValue time, TimeValue date);
	void setState(MarketStateEntry entry, boolean isOn);

	MarketDoBar loadBar(MarketField<MarketBar> barField);
	
}
