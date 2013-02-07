/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/**
 * 
 */
package com.barchart.feed.client.api;

import com.barchart.feed.client.enums.FeedState;

/**
 * Provides a callback method for changes in the 'logged-in' state of a data
 * feed. Users wishing to perform actions on login or log off should instantiate
 * this interface and bind it to a feed client.
 * <p>
 * Note: A feed state event will only fire when the state changes. If a state
 * listener is bound to a feed after login, no login state event will fire.
 * 
 */
public interface FeedStateListener {

	public void stateUpdate(FeedState state);

}
