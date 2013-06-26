package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Book extends MarketData<Book> {

	interface Top extends Existential {

		PriceLevel bid();

		PriceLevel ask();

		@Override
		boolean isNull();
		
		public static final Top NULL = new Top() {

			@Override
			public PriceLevel bid() {
				return PriceLevel.NULL;
			}

			@Override
			public PriceLevel ask() {
				return PriceLevel.NULL;
			}

			@Override
			public boolean isNull() {
				return true;
			}

		};

	}
	
	interface PriceLevel extends Existential, Tuple {

		@Override
		Price price();

		@Override
		Size size();

		Book.Side side();

		int level();

		@Override
		boolean isNull();

		final PriceLevel NULL = new PriceLevel() {

			@Override
			public Price price() {
				return Price.NULL;
			}

			@Override
			public Size size() {
				return Size.NULL;
			}

			@Override
			public Book.Side side() {
				return Book.Side.NULL;
			}

			@Override
			public int level() {
				return 0;
			}

			@Override
			public boolean isNull() {
				return true;
			}

		};

	}

	enum Side  {

		/** buy side */
		BID, //

		/** offer side */
		ASK, //
		
		NULL

		;

		public final byte ord = (byte) ordinal();

		private static final Side[] ENUM_VALUES = values();

		public static final Side fromOrd(final byte ord) {
			return ENUM_VALUES[ord];
		}

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
	
	Top top();

	List<PriceLevel> entryList(Side side);

	PriceLevel lastBookUpdate();

	public static final Book NULL = new Book() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
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
		public Top top() {
			return Top.NULL;
		}

		@Override
		public List<PriceLevel> entryList(final Side side) {
			return new ArrayList<PriceLevel>();
		}

		@Override
		public PriceLevel lastBookUpdate() {
			return PriceLevel.NULL;
		}

		@Override
		public Book freeze() {
			return this;
		}

	};

}
