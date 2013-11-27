package com.barchart.feed.base.values.provider;

import java.util.ArrayList;

import com.barchart.feed.base.values.api.Schedule;
import com.barchart.feed.base.values.api.TimeInterval;

public class BaseSchedule extends ArrayList<TimeInterval> implements Schedule {

	private static final long serialVersionUID = 1544081890014047813L;

	@Override
	public Schedule freeze() {
		
		final Schedule newSchedule = new BaseSchedule();
		for(final TimeInterval ti : this) {
			newSchedule.add(ti.freeze());
		}
		
		return newSchedule;
		
	}

	@Override
	public boolean isFrozen() {
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
