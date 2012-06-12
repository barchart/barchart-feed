/**
 * 
 */
package com.barchart.feed.client.api;

import com.barchart.feed.client.enums.FeedState;

/**
 * 
 */
public interface FeedStateListener {

	public void stateUpdate(FeedState state);

}
