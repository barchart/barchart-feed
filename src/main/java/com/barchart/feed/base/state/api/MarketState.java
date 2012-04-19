/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.state.api;

import java.util.Set;

import com.barchart.feed.base.state.enums.MarketStateEntry;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/**
 */
@NotMutable
public interface MarketState extends Value<MarketState>, Set<MarketStateEntry> {

}
