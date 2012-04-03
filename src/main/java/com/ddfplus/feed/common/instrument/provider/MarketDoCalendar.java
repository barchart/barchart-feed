package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.CalendarField;
import com.ddfplus.feed.api.instrument.values.MarketCalendar;

@Mutable
public interface MarketDoCalendar extends MarketCalendar {

	<V extends Value<V>> void set(CalendarField<V> field, V value);

}
