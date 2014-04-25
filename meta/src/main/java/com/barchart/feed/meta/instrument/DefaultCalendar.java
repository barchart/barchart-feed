package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.Event.Type;

public class DefaultCalendar extends HashMap<Event.Type, Event> implements Calendar {

	private static final long serialVersionUID = 1L;

	public DefaultCalendar() {
	}

	public DefaultCalendar(final List<Event> events) {
		for (final Event e : events) {
			add(e);
		}
	}

	public void add(final Event event) {
		put(event.type(), event);
	}

	@Override
	public List<Event> events() {
		return new ArrayList<Event>(values());
	}

	@Override
	public Event event(final Type type) {
		return get(type);
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
