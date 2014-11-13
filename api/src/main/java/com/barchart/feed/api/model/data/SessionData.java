package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.data.parameter.ParamMap;
import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Existential;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface SessionData extends Existential {

	// TODO Review for later
	interface Settle extends Comparable<Settle> {

		enum SettleType {
			NULL, PRELIM, FINAL, THEO
		}

		SettleType type();
		Price settle();
		Time time();

		/**
		 * Compares time of settle
		 */
		@Override
		int compareTo(Settle that);

	}

	Price open();

	Price high();

	Price low();

	Price close();

	Price previousClose();

	/**
	 * @return The last settle received in this session
	 */
	Price settle();

	/**
	 * True if the final settlement price has been received (for applicable
	 * markets.)
	 */
	Bool isSettled();

	/**
	 * @return Lists ordered by time of settle
	 */
	// TODO Review for later
	/*List<Settle> settles();*/

	Size volume();

	Size interest();

	/**
	 * The time that this session opened. Currently, this only represents the
	 * day that the session happened, as midnight UTC time.
	 */
	Time timeOpened();

	/**
	 * The time that this session opened. Currently, this only represents the
	 * day that the session happened, as midnight UTC time.
	 */
	Time timeClosed();

	Time updated();
	
	ParamMap parameters();
	
	@Override
	boolean isNull();

	public static final SessionData NULL = new SessionData() {

		@Override
		public Price open() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price high() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price low() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price close() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price previousClose() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price settle() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Bool isSettled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Size volume() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Size interest() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time timeOpened() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time timeClosed() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time updated() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public ParamMap parameters() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean isNull() {
			return true;
		}

	};

}
