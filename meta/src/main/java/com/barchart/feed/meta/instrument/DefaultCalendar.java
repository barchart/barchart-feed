package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.Event.Type;

public class DefaultCalendar extends ArrayList<Event> implements Calendar {

	private static final long serialVersionUID = 1L;

	public DefaultCalendar() {
	}

	public DefaultCalendar(final List<Event> events) {
		addAll(events);
	}

	@Override
	public List<Event> events() {
		return Collections.unmodifiableList(this);
	}

	@Override
	public Event event(final Type type) {

		for (final Event evt : this) {
			if (evt.type() == type)
				return evt;
		}

		return null;

	}

	@Override
	public boolean isNull() {
		return false;
	}

}
