/**
 * 
 */
package com.barchart.feed.client.api;

import com.barchart.util.values.api.TimeValue;

/**
 * Provides a callback method which will fire when the feed receives a time
 * stamp message from a data source.
 * 
 */
public interface TimestampListener {

	public void handleTimestamp(TimeValue timestamp);

}
