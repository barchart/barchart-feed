/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.api;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.util.common.anno.NotMutable;

@NotMutable
public interface TimeValue extends Value<TimeValue>, Comparable<TimeValue> {

	//

	/** in zone UTC */
	@Deprecated
	Date asDate();

	/** in zone UTC */
	DateTime asDateTime();

	DateTime asDateTime(DateTimeZone zone);

	/**
	 * milliseconds since January 1, 1970, 00:00:00 GMT
	 **/
	long asMillisUTC();

	//

	@Override
	int compareTo(TimeValue thatTime);

	@Override
	int hashCode();

	@Override
	boolean equals(Object thatTime);

	//
}
