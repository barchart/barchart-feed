/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.util.common.anno.NotMutable;

@NotMutable
abstract class BaseTime extends ValueFreezer<TimeValue> implements TimeValue {

	//

	@Override
	public abstract long asMillisUTC();

	//

	@Override
	public final int compareTo(final TimeValue that) {
		final long t1 = this.asMillisUTC();
		final long t2 = that.asMillisUTC();
		return t1 < t2 ? -1 : (t1 == t2 ? 0 : 1);
	}

	@Override
	public final int hashCode() {
		final long millis = this.asMillisUTC();
		return (int) (millis ^ (millis >>> 32));
	}

	@Override
	public final boolean equals(final Object thatTime) {
		if (thatTime instanceof TimeValue) {
			final TimeValue that = (TimeValue) thatTime;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final Date asDate() {
		return asDateTime().toDate();
	}

	@Override
	public final DateTime asDateTime() {
		return new DateTime(asMillisUTC(), DateTimeZone.UTC);
	}

	@Override
	public final DateTime asDateTime(final DateTimeZone zone) {
		return new DateTime(asMillisUTC(), zone);
	}

	@Override
	public final String toString() {
		return String.format("Time > %30s", asDateTime());
	}

	@Override
	public final boolean isNull() {
		return this == ValueConst.NULL_TIME;
	}

}
