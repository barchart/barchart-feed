package com.barchart.feed.api.model.data;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.ChangeSet;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session>, SessionData, 
		ChangeSet<Session.Component> {

	/**
	 * Last changed session item.
	 */
	enum Component {

		UNKNOWN, //

		OPEN, //
		HIGH, //
		LOW, //
		CLOSE, //

		BID, //
		ASK, //

		SETTLE, //

		VOLUME, //
		INTEREST, //

//		TIME_OPENED, //
//		TIME_UPDATED, //
//		TIME_CLOSED, //

		;

	}
	
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

	/**
	 * @return the close of the previous trading day.
	 */
	Price previousClose();
	
	/** FIXME kill */
	boolean isSettled();

	@Override
	Set<Component> change();
	
	@Override
	Instrument instrument();
	
	@Override
	Price open();

	@Override
	Price high();

	@Override
	Price low();

	@Override
	Price close();

	@Override
	Price settle();

	@Override
	Size volume();

	@Override
	Size interest();

	@Override
	Time timeOpened();

	@Override
	Time timeUpdated();

	@Override
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
		public String toString() {
			return "NULL SESSION";
		}

		@Override
		public Price previousClose() {
			return Price.NULL;
		}

		@Override
		public Set<Component> change() {
			return Collections.emptySet();
		}

	};

}
