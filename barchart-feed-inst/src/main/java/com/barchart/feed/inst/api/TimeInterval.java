package com.barchart.feed.inst.api;

import com.barchart.util.values.api.Value;

public class TimeInterval implements Value<TimeInterval> {

	public static final TimeInterval NULL_INTERVAL = new TimeInterval(0,0);
	
	private final long begin, end;
	
	public TimeInterval(final long begin, final long end) {
		this.begin = begin;
		this.end = end;
	}

	public final long getBegin() {
		return begin;
	}

	public final long getEnd() {
		return end;
	}

	@Override
	public TimeInterval freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_INTERVAL;
	}
	
}
