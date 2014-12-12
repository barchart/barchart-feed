package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.Event.Type;
import com.barchart.feed.api.model.meta.instrument.ModifiableCalendar;

public class DefaultCalendar implements ModifiableCalendar {

	private final List<Event> events = new ArrayList<Event>();
	
	public DefaultCalendar() {
	}

	public DefaultCalendar(final List<Event> newEvents) {
		events.addAll(newEvents);
	}

	@Override
	public void add(final Event event) {
		events.add(event);
	}
	
	@Override
	public void addAll(final Collection<Event> newEvents) {
		events.addAll(newEvents);
	}
	
	@Override
	public List<Event> events() {
		return Collections.unmodifiableList(events);
	}

	@Override
	public Event event(final Type type) {

		for (final Event evt : events) {
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
