/**
 * 
 */
package com.barchart.feed.base.market;

import com.barchart.feed.base.bar.api.MarketDoBar;
import com.barchart.feed.base.bar.enums.MarketBarType;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.cuvol.api.MarketDoCuvolEntry;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.provider.VarMarket;
import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.trade.enums.MarketTradeSequencing;
import com.barchart.feed.base.trade.enums.MarketTradeSession;
import com.barchart.feed.base.trade.enums.MarketTradeType;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;

/**
 * @author g-litchfield
 * 
 */
public class MockVarMarket extends VarMarket {

	/**
	 * 
	 */
	MockVarMarket() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setInstrument
	 * (com.barchart.feed.base.api.instrument.values.MarketInstrument)
	 */
	@Override
	public void setInstrument(final MarketInstrument symbol) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setBookUpdate
	 * (com.barchart.feed.base.provider.market.api.MarketDoBookEntry,
	 * com.barchart.util.values.api.TimeValue)
	 */
	@Override
	public void setBookUpdate(final MarketDoBookEntry entry,
			final TimeValue time) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setBookSnapshot
	 * (com.barchart.feed.base.provider.market.api.MarketDoBookEntry[],
	 * com.barchart.util.values.api.TimeValue)
	 */
	@Override
	public void setBookSnapshot(final MarketDoBookEntry[] entries,
			final TimeValue time) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setCuvolUpdate
	 * (com.barchart.feed.base.provider.market.provider.MarketDoCuvolEntry,
	 * com.barchart.util.values.api.TimeValue)
	 */
	@Override
	public void setCuvolUpdate(final MarketDoCuvolEntry entry,
			final TimeValue time) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setCuvolSnapshot
	 * (com.barchart.feed.base.provider.market.provider.MarketDoCuvolEntry[],
	 * com.barchart.util.values.api.TimeValue)
	 */
	@Override
	public void setCuvolSnapshot(final MarketDoCuvolEntry[] entries,
			final TimeValue time) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setBar(com.barchart
	 * .feed.base.api.market.enums.MarketBarType,
	 * com.barchart.feed.base.provider.market.provider.MarketDoBar)
	 */
	@Override
	public void setBar(final MarketBarType type, final MarketDoBar bar) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setTrade(com
	 * .barchart.feed.base.api.market.enums.MarketBarType,
	 * com.barchart.util.values.api.PriceValue,
	 * com.barchart.util.values.api.SizeValue,
	 * com.barchart.util.values.api.TimeValue)
	 */
	@Override
	public void setTrade(final MarketTradeType type,
			final MarketTradeSession session,
			final MarketTradeSequencing sequencing, final PriceValue price,
			final SizeValue size, final TimeValue time, final TimeValue date) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.barchart.feed.base.provider.market.provider.MarketDo#setState(com
	 * .barchart.feed.base.api.market.enums.MarketStateEntry, boolean)
	 */
	@Override
	public void setState(final MarketStateEntry entry, final boolean isOn) {
		// TODO Auto-generated method stub

	}

}
