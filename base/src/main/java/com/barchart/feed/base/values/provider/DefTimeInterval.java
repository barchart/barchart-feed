/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.TimeInterval;
import com.barchart.feed.base.values.api.TimeValue;

public class DefTimeInterval extends ValueFreezer<TimeInterval> implements TimeInterval {

	final long start;
	final long stop;
	
	DefTimeInterval(final TimeValue start, final TimeValue stop) {
		this.start = start.asMillisUTC();
		this.stop = stop.asMillisUTC();
	}
	
	DefTimeInterval(final long start, final long stop) {
		this.start = start;
		this.stop = stop;
	}
	
	@Override
	public boolean isNull() {
		return this == ValueConst.NULL_TIME_INTERVAL;
	}

	@Override
	public TimeValue start() {
		return ValueBuilder.newTime(start);
	}

	@Override
	public long startAsMillis() {
		return start;
	}

	@Override
	public TimeValue stop() {
		return ValueBuilder.newTime(stop);
	}

	@Override
	public long stopAsMillis() {
		return stop;
	}
	
	@Override
	public boolean equals(final Object o) {
		
		if(o == null) {
			return false;
		}
		
		if(this == o) {
			return true;
		}
		
		if(!(o instanceof TimeInterval)) {
			return false;
		}
		
		final TimeInterval tI = (TimeInterval)o;
		
		if(this.isNull()) {
			if(tI.isNull()) {
				return true;
			} else {
				return false;
			}
		}
		
		if(tI.isNull()) {
			return false;
		}
		
		return (start == tI.startAsMillis()) &&
				(stop == tI.stopAsMillis());
		
	}

}
