/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.instrument.enums;

import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.collections.BitSetEnum;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;

/**
 * represents instrument time events;
 * 
 * all times are in UTC;
 */
@NotMutable
public final class CalendarField<V extends Value<V>> extends
		ParaEnumBase<V, CalendarField<V>> implements
		BitSetEnum<CalendarField<?>> {

	// ##################################

	/**
	 * market instrument local time zone; can be used to render time in market's
	 * local time;
	 * 
	 * http://joda-time.sourceforge.net/timezones.html
	 */
	public static final CalendarField<TextValue> TIME_ZONE = NEW(NULL_TEXT);

	/** market open time on normal business day */
	public static final CalendarField<TimeValue> TIME_OPEN = NEW(NULL_TIME);

	/** market close time on normal business day */
	public static final CalendarField<TimeValue> TIME_CLOSE = NEW(NULL_TIME);

	/** instrument initiation trading date; such as option issue date */
	public static final CalendarField<TimeValue> DATE_START = NEW(NULL_TIME);

	/** instrument termination trading date; such as future expiration month */
	public static final CalendarField<TimeValue> DATE_FINISH = NEW(NULL_TIME);

	// ##################################

	public static int size() {
		return values().length;
	}

	public static CalendarField<?>[] values() {
		return DictEnum.valuesForType(CalendarField.class);
	}

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}

	private CalendarField() {
		super();
		mask = 0;
	}

	private CalendarField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}

	private static final <X extends Value<X>> CalendarField<X> NEW(X value) {
		return new CalendarField<X>(value);
	}

}
