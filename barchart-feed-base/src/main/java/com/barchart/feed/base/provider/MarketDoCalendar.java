/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import com.barchart.feed.base.instrument.enums.CalendarField;
import com.barchart.feed.base.instrument.values.MarketCalendar;
import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.Value;

@Mutable
interface MarketDoCalendar extends MarketCalendar {

	<V extends Value<V>> void set(CalendarField<V> field, V value);

}
