/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.values;

import com.barchart.feed.base.instrument.enums.CalendarField;
import com.barchart.util.values.api.Value;

/** market activity schedule in time */
public interface MarketCalendar extends Value<MarketCalendar> {

	<V extends Value<V>> V get(CalendarField<V> field);

}
