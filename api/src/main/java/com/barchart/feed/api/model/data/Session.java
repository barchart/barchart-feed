package com.barchart.feed.api.model.data;

import java.util.Set;

import com.barchart.feed.api.model.ChangeSet;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Bool;
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

	@Override
	Price previousClose();
	
	@Override
	Bool isSettled();

	@Override
	Time updated();
	
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
	Time timeClosed();
	
	public static final Session NULL = new Session() {

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
		public Price settle() {
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
		public Bool isSettled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price previousClose() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<Component> change() {
			throw new UnsupportedOperationException();
		}

	};

}
