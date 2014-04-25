package com.barchart.feed.api.model.meta.instrument;

import java.util.Collections;
import java.util.List;

import com.barchart.feed.api.model.meta.instrument.Event.Type;
import com.barchart.util.value.api.Existential;

/**
 * A calendar of trading events, containing notable events in an instrument's
 * lifecycle (first trade date, last trade date / expiration, etc.)
 */
public interface Calendar extends Existential {

	/**
	 * Get all available events.
	 */
	List<Event> events();

	/**
	 * Get a specific event type, or null if it does not exist.
	 */
	Event event(Event.Type type);

	static Calendar NULL = new Calendar() {

		@Override
		public List<Event> events() {
			return Collections.emptyList();
		}

		@Override
		public Event event(final Type type) {
			return Event.NULL;
		}

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
