package com.barchart.feed.api.model.data;

import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Trade extends MarketData<Trade>, Tuple {

	enum Sequence {
		NULL, NORMAL, UNSEQUENCED
	}
	
	enum Session {
		NULL, DEFAULT, PIT, COMBINED, EXTENDED
	}
	
	enum TradeType {
		
		// Null trade type
		NULL_TRADE_TYPE(Session.DEFAULT, Sequence.NORMAL),

		// Unknown trade type
		UNKNOWN(Session.DEFAULT, Sequence.NORMAL),

		/** FUTURES */

		// Composite session (pit + electronic)
		FUTURE_COMPOSITE(Session.COMBINED, Sequence.NORMAL),
		// Electronic/overnight session
		FUTURE_ELECTRONIC(Session.DEFAULT, Sequence.NORMAL),
		// Pit / open outcry session
		FUTURE_PIT(Session.PIT, Sequence.NORMAL),

		/** EQUITIES */

		// Regular trade types
		REGULAR_SALE(Session.DEFAULT, Sequence.NORMAL),
		ACQUISITION(Session.DEFAULT, Sequence.NORMAL),
		AMEX_RULE_155(Session.DEFAULT, Sequence.NORMAL),
		AUTOMATIC_EXECUTION(Session.DEFAULT, Sequence.NORMAL),
		BUNCHED_TRADE(Session.DEFAULT, Sequence.NORMAL),
		CROSS_TRADE(Session.DEFAULT, Sequence.NORMAL),
		DISTRIBUTION(Session.DEFAULT, Sequence.NORMAL),
		INTERMARKET_SWEEP(Session.DEFAULT, Sequence.NORMAL),
		MARKET_CLOSING(Session.DEFAULT, Sequence.NORMAL),
		MARKET_OPENING(Session.DEFAULT, Sequence.UNSEQUENCED),
		MARKET_REOPENING(Session.DEFAULT, Sequence.NORMAL),
		NYSE_RULE_127(Session.DEFAULT, Sequence.NORMAL),
		RESERVED(Session.DEFAULT, Sequence.NORMAL),
		SPLIT(Session.DEFAULT, Sequence.NORMAL),
		STOCK_OPTION(Session.DEFAULT, Sequence.NORMAL),
		STOPPED_STOCK_REGULAR(Session.DEFAULT, Sequence.NORMAL),
		YELLOW_FLAG(Session.DEFAULT, Sequence.NORMAL),

		// Regular out-of-sequence trade types
		BUNCHED_SOLD(Session.DEFAULT, Sequence.UNSEQUENCED),
		DERIVATIVELY_PRICED(Session.DEFAULT, Sequence.UNSEQUENCED),
		PRIOR_REFERENCE_PRICE(Session.DEFAULT, Sequence.UNSEQUENCED),
		SOLD_LAST(Session.DEFAULT, Sequence.UNSEQUENCED),
		SOLD_OOO(Session.DEFAULT, Sequence.UNSEQUENCED),
		STOPPED_STOCK_OOO(Session.DEFAULT, Sequence.UNSEQUENCED),
		STOPPED_STOCK_SOLD_LAST(Session.DEFAULT, Sequence.UNSEQUENCED),

		// Form-T (pre/post-market) trades
		FORM_T(Session.EXTENDED, Sequence.NORMAL),
		FORM_T_OOO(Session.EXTENDED, Sequence.UNSEQUENCED)
		
		;
		
		public final Session session;
		public final Sequence sequence;
		
		private TradeType(Session session, Sequence sequence) {
			this.session = session;
			this.sequence = sequence;
		}
	}
	
	Session session();
	
	Sequence sequence();
	
	Set<TradeType> types();
	
	@Override
	Price price();

	@Override
	Size size();

	/**
	 * 
	 * @return The time the trade occurred at the exchange.
	 */
	Time time();

	public static final Trade NULL = new Trade() {
		
		@Override
		public Session session() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Sequence sequence() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Set<TradeType> types() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Instrument instrument() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time updated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price price() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Size size() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time time() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

	};

}
