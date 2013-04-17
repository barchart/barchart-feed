package com.barchart.feed.api.commons;

import java.util.ArrayList;

import com.barchart.util.values.api.TimeInterval;

@SuppressWarnings("serial")
public class Schedule extends ArrayList<TimeInterval> {

	public Schedule(final TimeInterval[] intervals) {
		super(intervals.length);
		for(TimeInterval ti : intervals) {
			add(ti);
		}
	}
	
}
