/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.message;

import com.barchart.feed.base.instrument.values.MarketInstrument;

public interface MarketMessage {

	// TimeValue getTime();

	MarketInstrument getInstrument();

}
