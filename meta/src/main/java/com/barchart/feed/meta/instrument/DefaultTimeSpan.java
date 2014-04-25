package com.barchart.feed.meta.instrument;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.instrument.TimeSpan;

public class DefaultTimeSpan implements TimeSpan {

	private final DateTime start;
	private final DateTime stop;

	public DefaultTimeSpan(final long start_, final long stop_) {
		this(new DateTime(start_), new DateTime(stop_));
	}

	public DefaultTimeSpan(final DateTime start_, final DateTime stop_) {
		start = new DateTime(start_);
		stop = new DateTime(stop_);
	}

	@Override
	public DateTime start() {
		return start;
	}

	@Override
	public DateTime stop() {
		return stop;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
