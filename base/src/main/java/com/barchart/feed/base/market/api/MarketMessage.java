/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.api;

import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.util.values.api.TimeValue;

public interface MarketMessage {

	TimeValue getTime();

	InstrumentEntity getInstrument();

}
