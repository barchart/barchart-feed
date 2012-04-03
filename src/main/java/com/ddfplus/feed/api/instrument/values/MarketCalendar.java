package com.ddfplus.feed.api.instrument.values;

import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.CalendarField;

/** market activity schedule in time */
public interface MarketCalendar extends Value<MarketCalendar> {

	<V extends Value<V>> V get(CalendarField<V> field);

}
