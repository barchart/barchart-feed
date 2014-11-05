package com.barchart.feed.api.model.data;

import java.util.Set;

import com.barchart.feed.api.model.ChangeSet;
import com.barchart.feed.api.model.data.Session.Type;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface SessionSet extends MarketData<SessionSet>, ChangeSet<Type> {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	SessionData session(Session.Type type);

	@Override
	Set<Type> change();
	
	@Override
	Instrument instrument();

	@Override
	Time updated();

	@Override
	boolean isNull();
	
	SessionSet NULL = new SessionSet() {

		@Override
		public SessionData session(Type type) {
			throw new UnsupportedOperationException();
		}

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
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<Type> change() {
			throw new UnsupportedOperationException();
		}
		
	};
}
