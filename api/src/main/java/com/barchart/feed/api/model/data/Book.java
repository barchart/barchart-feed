package com.barchart.feed.api.model.data;

import java.util.ArrayList;
import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Book extends MarketData<Book> {

	interface Top extends Existential {

		Entry bid();

		Entry ask();

		@Override
		boolean isNull();

		Top NULL = new Top() {

			@Override
			public Entry bid() {
				return Entry.NULL;
			}

			@Override
			public Entry ask() {
				return Entry.NULL;
			}

			@Override
			public boolean isNull() {
				return true;
			}
			
			@Override
			public String toString() {
				return "NULL BOOK TOP";
			}

		};

	}

	interface Entry extends Existential, Tuple {

		@Override
		Price price();

		@Override
		Size size();

		Book.Side side();

		int level();

		@Override
		boolean isNull();

		Entry NULL = new Entry() {

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
			
			@Override
			public String toString() {
				return "NULL BOOK ENTRY";
			}

		};

	}

	/** TODO simplify for consumer */
	enum Side {

		/** buy side */
		BID, //

		/** offer side */
		ASK, //

		/** ??? kill */
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

	List<Entry> entryList(Side side);

	Entry lastBookUpdate();

	Book NULL = new Book() {

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
		public List<Entry> entryList(final Side side) {
			return new ArrayList<Entry>();
		}

		@Override
		public Entry lastBookUpdate() {
			return Entry.NULL;
		}

		@Override
		public Book freeze() {
			return this;
		}
		
		@Override
		public String toString() {
			return "NULL BOOK";
		}

	};

}
