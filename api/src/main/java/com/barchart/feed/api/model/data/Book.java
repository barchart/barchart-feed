package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.PriceLevel;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface Book extends MarketData<Book> {

	interface Top {

	}

	/**
	 * Book liquidity type.
	 */
	enum Type {

		/** 	*/
		NONE,
		
		/**		 */
		DEFAULT, //

		/**		 */
		IMPLIED, //

		/**		 */
		COMBINED; //

		public final byte ord = (byte) ordinal();
		
		private static final Type[] ENUM_VALUES = values();
		
		public static final Type fromOrd(final byte ord) {
			return ENUM_VALUES[ord];
		}

		public static final Type fromText(final String type) {
			for (final Type t : values()) {
				if (type.compareTo(t.name()) == 0) {
					return t;
				}
			}
			return NONE;
		}
		
	}
	
	/**
	 * Book side.
	 */
	enum Side {
		BID, //
		ASK, //
	}
	
	TopOfBook topOfBook();

	List<PriceLevel> entryList(MarketSide side);

	PriceLevel lastBookUpdate();

	public static final Book NULL_ORDERBOOK = new Book() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL_INSTRUMENT;
		}

		@Override
		public Time updated() {
			return Time.NULL;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Book copy() {
			return this;
		}

		@Override
		public TopOfBook topOfBook() {
			return TopOfBook.NULL_TOP_OF_BOOK;
		}

		@Override
		public List<PriceLevel> entryList(final MarketSide side) {
			return new ArrayList<PriceLevel>();
		}

		@Override
		public PriceLevel lastBookUpdate() {
			return PriceLevel.NULL_PRICE_LEVEL;
		}

	};

}
