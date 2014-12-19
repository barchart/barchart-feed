package com.barchart.feed.api.model.data;

import java.util.List;
import java.util.Set;

import com.barchart.feed.api.model.ChangeSet;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.Tuple;

public interface Book extends MarketData<Book>, ChangeSet<Book.Component> {

	/**
	 * Last changed book entry.
	 */
	enum Component {
		NORMAL_BID, //
		NORMAL_ASK, //
		TOP_BID, //
		TOP_ASK, //
		ANY_BID, //
		ANY_ASK, //
	}
	
	interface Top extends Existential {

		Entry bid();

		Entry ask();

		@Override
		boolean isNull();

		Top NULL = new Top() {

			@Override
			public Entry bid() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Entry ask() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isNull() {
				return true;
			}
			
			@Override
			public String toString() {
				throw new UnsupportedOperationException();
			}

		};

	}

	interface Entry extends Existential, Tuple, Comparable<Entry> {

		@Override
		Price price();

		@Override
		Size size();
		
		/**
		 * Compare is made on Price
		 */
		@Override
		int compareTo(Entry entry);

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
			public int compareTo(final Entry o) {
				return Price.NULL.compareTo(o.price());
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
		BID(1), //

		/** offer side */
		ASK(-1), //

		/** ??? kill */
		NULL(0)

		;
		
		public final int sign;
		
		private Side(final int sign) {
			this.sign = sign;
		}

		public final byte ord = (byte) ordinal();

		private static final Side[] ENUM_VALUES = values();

		public static final Side fromOrd(final byte ord) {
			return ENUM_VALUES[ord];
		}
		
		public final Side opp() {
			return this == NULL ? NULL : this == BID ? ASK : BID;
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
	
	@Override
	Set<Component> change();
	
	@Override
	Instrument instrument();

	Book NULL = new Book() {

		@Override
		public Instrument instrument() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time updated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Top top() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Entry> entryList(final Side side) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry lastBookUpdate() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<Component> change() {
			throw new UnsupportedOperationException();
		}

	};

}
