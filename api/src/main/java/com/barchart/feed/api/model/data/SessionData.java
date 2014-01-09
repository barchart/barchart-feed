package com.barchart.feed.api.model.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface SessionData {
	
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

	/**
	 * @return The last settle recieved in this session
	 */
	Price settle();
	
	/**
	 * @return Lists ordered by time of settle
	 */
	// TODO Review for later
	/*List<Settle> settles();*/

	Size volume();

	Size interest();

	Time timeOpened();

	Time timeClosed();
	
	public static final SessionData NULL = new SessionData() {
		
		@Override
		public Price open() {
			return Price.NULL;
		}

		@Override
		public Price high() {
			return Price.NULL;
		}

		@Override
		public Price low() {
			return Price.NULL;
		}

		@Override
		public Price close() {
			return Price.NULL;
		}

		@Override
		public Price settle() {
			return Price.NULL;
		}

		@Override
		public Size volume() {
			return Size.NULL;
		}

		@Override
		public Size interest() {
			return Size.NULL;
		}

		@Override
		public Time timeOpened() {
			return Time.NULL;
		}

		@Override
		public Time timeClosed() {
			return Time.NULL;
		}
		
	};
	
}
