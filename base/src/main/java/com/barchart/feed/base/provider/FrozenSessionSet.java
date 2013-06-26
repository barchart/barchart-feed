package com.barchart.feed.base.provider;

import java.util.EnumMap;

import com.barchart.feed.api.model.data.Session;
import com.barchart.feed.api.model.data.Session.Type;
import com.barchart.feed.api.model.data.SessionSet;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

class FrozenSessionSet implements SessionSet {
	
	private final Instrument instrument;
	
	private final EnumMap<Session.Type, Session> map = 
			new EnumMap<Session.Type, Session>(Session.Type.class);
	
	
	FrozenSessionSet(final Instrument instrument, 
			final Session current, 
			final Session currentExtended,
			final Session previous, 
			final Session previousExtended) {
		
		this.instrument = instrument;
		
		map.put(Session.Type.DEFAULT_CURRENT, current);
		map.put(Session.Type.DEFAULT_PREVIOUS, previous);
		map.put(Session.Type.EXTENDED_CURRENT, currentExtended);
		map.put(Session.Type.EXTENDED_PREVIOUS, previousExtended);
		
	}

	@Override
	public Time updated() {
		return null;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public Session session(Type type) {
		return map.get(type);
	}

	@Override
	public SessionSet freeze() {
		return this;
	}

	
}
