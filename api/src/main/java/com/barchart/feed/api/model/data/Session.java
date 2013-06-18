package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.MarketData;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

/**
 * document object and primitive
 */
public interface Session extends MarketData<Session>, SessionData {

	SessionData extended();
	
	SessionData previous();
	
	SessionData previousExtended();
	
	boolean isSettled();

	public static final Session NULL_SESSION = new Session() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL_INSTRUMENT;
		}

		@Override
		public Time updated() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Session copy() {
			return this;
		}

		@Override
		public Price open() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price high() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price low() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price close() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price settle() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Size volume() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public Size interest() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public Time timeOpened() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public Time timeUpdated() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public Time timeClosed() {
			return ValueConst.NULL_TIME;
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
		
	};
	
}
