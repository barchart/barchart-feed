package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session> {

	/**
	 * Market session type.
	 */
	enum Type {

		/**
		 * Live open session.
		 * <p>
		 * Lifetime is 1 day.
		 */
		DEFAULT_CURRENT, //

		/**
		 * Snapshot of {@link #CURRENT} at the moment of its closing.
		 * <p>
		 * Lifetime is 1 day.
		 */
		DEFAULT_PREVIOUS, //

		/**
		 * Represents FORM-T live open session.
		 * <p>
		 * TODO define exact life cycle.
		 */
		EXTENDED_CURRENT, //

		/**
		 * Represents FORM-T past closed session.
		 * <p>
		 * TODO define exact life cycle.
		 */
		EXTENDED_PREVIOUS, //

	}
	
	/** FIXME kill */
	boolean isSettled();

	/** FIXME report change set. */
	// Set<Component> change();
	
	Price open();

	Price high();

	Price low();

	Price close();

	Price settle();

	Size volume();

	Size interest();

	Time timeOpened();

	Time timeUpdated();

	Time timeClosed();

	public static final Session NULL = new Session() {

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
		public Time timeUpdated() {
			return Time.NULL;
		}

		@Override
		public Time timeClosed() {
			return Time.NULL;
		}

		@Override
		public boolean isSettled() {
			return false;
		}

		@Override
		public Session freeze() {
			return this;
		}
		
		@Override
		public String toString() {
			return "NULL SESSION";
		}

	};

}
