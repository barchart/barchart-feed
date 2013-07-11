package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.data.Session.Type;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface SessionSet extends MarketData<SessionSet> {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	SessionData session(Session.Type type);

	@Override
	Instrument instrument();

	@Override
	Time updated();

	@Override
	SessionSet freeze();
	
	@Override
	boolean isNull();
	
	SessionSet NULL = new SessionSet() {

		@Override
		public SessionData session(Type type) {
			return SessionData.NULL;
		}

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
		}

		@Override
		public Time updated() {
			return Time.NULL;
		}

		@Override
		public SessionSet freeze() {
			return this;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
		@Override
		public String toString() {
			return "NULL SESSION SET";
		}
		
	};
}
