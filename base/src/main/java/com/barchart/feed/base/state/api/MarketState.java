/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.state.api;

import java.util.Set;

import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.feed.base.values.api.Value;
import com.barchart.util.anno.NotMutable;

/**
 */
@NotMutable
public interface MarketState extends Value<MarketState>, Set<MarketStateEntry> {

}
