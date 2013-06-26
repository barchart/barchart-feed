package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session>, SessionData {

	/** FIXME kill */
	SessionData extended();

	/** FIXME kill */
	SessionData previous();

	/** FIXME kill */
	SessionData previousExtended();

	/** FIXME kill */
	boolean isSettled();

	/** FIXME report change set. */
	// Set<Component> change();

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
		public SessionData extended() {
			return SessionData.NULL_SESSION_DATA;
		}

		@Override
		public SessionData previous() {
			return SessionData.NULL_SESSION_DATA;
		}

		@Override
		public SessionData previousExtended() {
			return SessionData.NULL_SESSION_DATA;
		}

		@Override
		public boolean isSettled() {
			return false;
		}

		@Override
		public Session freeze() {
			return this;
		}

	};

}
