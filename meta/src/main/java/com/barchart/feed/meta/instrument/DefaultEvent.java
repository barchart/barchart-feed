package com.barchart.feed.meta.instrument;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.instrument.Event;

public class DefaultEvent implements Event {

	private final Event.Type type;
	private final String description;
	private final DateTime date;

	public DefaultEvent(final Event.Type type_, final DateTime date_) {
		this(type_, type_.description(), date_);
	}

	public DefaultEvent(final Event.Type type_, final String description_, final DateTime date_) {
		type = type_;
		description = description_;
		date = date_;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public DateTime date() {
		return date;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
