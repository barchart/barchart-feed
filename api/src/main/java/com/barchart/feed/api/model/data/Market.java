package com.barchart.feed.api.model.data;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Time;

public interface Market extends MarketData<Market> {

	/**
	 * Last changed market component.
	 */
	enum Component {

		/** Trade update. */
		TRADE, //

		/** Book update. */
		BOOK_DEFAULT, //

		/** Book update. */
		BOOK_IMPLIED, //

		/** Book update. */
		BOOK_COMBINED, //

		/** Cuvol update. */
		CUVOL, //

		/** Session update. */
		DEFAULT_CURRENT, //

		/** Session update. */
		DEFAULT_PREVIOUS, //

		/** Session update. */
		EXTENDED_CURRENT, //

		/** Session update. */
		EXTENDED_PREVIOUS, //

		/** New instrument definition. */
		INSTRUMENT, //

		/** New market state snapshot. */
		MARKET_DATA, //

		/** Unsupported update. */
		UNKNOWN, //
	}

	/**
	 * Market change set.
	 * 
	 * @return will contain: empty - for unsupported update;
	 *         {@link Component#MARKET_DATA} - for complete snapshot;
	 *         {@link Component#INSTRUMENT} - for new definition; other - for
	 *         market data updates.
	 */
	Set<Component> change();
	
//	/**
//	 * The possible states a market can be in
//	 */
//	enum MarketState {
//		NULL, PRE_OPEN, OPEN, SUSPENDED, CLOSED
//	}
//	
//	/**
//	 * @return the current state of this market
//	 */
//	MarketState marketState();
	
	interface LastPrice extends Existential {
		
		/**
		 * In order of lowest to highest priority
		 */
		enum Source {

			NULL(""),

			PREV_CLOSE("p"),

			PREV_SETTLE("p"),

			LAST_TRADE(""),

			CLOSE(""),

			SETTLE("s");

			private final String flag;

			private Source(String flag_) {
				flag = flag_;
			}

			public String flag() {
				return flag;
			}

		}
		
		Source source();
		
		Price price();
		
		@Override
		boolean isNull();
		
		LastPrice NULL = new LastPrice() {

			@Override
			public Source source() {
				throw new UnsupportedOperationException("Null Last Price");
			}

			@Override
			public Price price() {
				throw new UnsupportedOperationException("Null Last Price");
			}
			
			@Override
			public boolean isNull() {
				return true;
			}
			
		};
		
	}
	
	LastPrice lastPrice();
	
	/** Last trade. */
	Trade trade();

	Book book();

	BookSet bookSet();

	Cuvol cuvol();

	Session session();

	SessionSet sessionSet();

	Market NULL = new Market() {

		@Override
		public Instrument instrument() {return Instrument.NULL;}

		@Override
		public Time updated() {return Time.NULL;}

		@Override
		public Trade trade() {return Trade.NULL;}

		@Override
		public Book book() {return Book.NULL;}

		@Override
		public BookSet bookSet() {return BookSet.NULL;}

		@Override
		public Cuvol cuvol() {return Cuvol.NULL;}

		@Override
		public Session session() {return Session.NULL;}

		@Override
		public SessionSet sessionSet() {return SessionSet.NULL;}

		@Override
		public boolean isNull() {return true;}
		
		@Override
		public String toString() {return "NULL_MARKET";}

		@Override
		public Set<Component> change() {return Collections.<Component> emptySet();}
		
		@Override
		public LastPrice lastPrice() {return LastPrice.NULL;}

	};

}
