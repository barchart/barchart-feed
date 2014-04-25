package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.TimeSpan;

public class DefaultSchedule extends ArrayList<TimeSpan> implements Schedule {

	private static final long serialVersionUID = 1L;

	public DefaultSchedule() {
		super();
	}

	public DefaultSchedule(final Collection<? extends TimeSpan> sessions) {
		super(sessions);
	}

	@Override
	public boolean isOpen(final DateTime instant) {

		final DateTime base = instant.withDayOfWeek(1).withTimeAtStartOfDay();
		final long relative = instant.getMillis() - base.getMillis();

		for (final TimeSpan span : this) {
			if (relative >= span.start().getMillis() && relative <= span.stop().getMillis()) {
				return true;
			}
		}

		return false;

	}

	@Override
	public boolean isNull() {
		return false;
	}

}
