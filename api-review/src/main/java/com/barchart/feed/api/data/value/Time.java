package com.barchart.feed.api.data.value;

import com.barchart.feed.api.util.Copyable;

public interface Time extends Comparable<Time>, Copyable<Time> {

	// TODO what class are we going to expose here?  Date, DateTime, or just helper methods?
	
	/**
	 * milliseconds since January 1, 1970, 00:00:00 GMT
	 **/
	long asMillisUTC();

	//

	@Override
	int compareTo(Time thatTime);

	@Override
	int hashCode();

	@Override
	boolean equals(Object thatTime);
	
}
