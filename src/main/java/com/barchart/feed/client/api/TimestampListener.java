/**
 * 
 */
package com.barchart.feed.client.api;

import com.barchart.util.values.api.TimeValue;

/**
 * @author g-litchfield
 * 
 */
public interface TimestampListener {

	public void handleTimestamp(TimeValue timestamp);

}
