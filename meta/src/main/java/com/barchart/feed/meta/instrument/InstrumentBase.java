package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.Event.Type;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public abstract class InstrumentBase implements Instrument {

	protected static final ValueFactory VALUES = ValueFactoryImpl.getInstance();

	/*
	 * Common methods
	 */

	@Override
	public MetaType type() {
		return MetaType.INSTRUMENT;
	}

	@Override
	public boolean isNull() {
		return this == Instrument.NULL;
	}

	/*
	 * Aliases
	 */

	@Override
	public DateTime delivery() {

		final Event event = calendar().event(Type.LAST_DELIVERY_DATE);

		if (event == null || event.isNull()) {
			return null;
		}

		return event.date();

	}

	@Override
	public DateTime expiration() {

		final Event event = calendar().event(Type.LAST_TRADE_DATE);

		if (event == null || event.isNull()) {
			return null;
		}

		return event.date();

	}

	/*
	 * Object comparisons
	 */

	@Override
	public String toString() {
		return symbol();
	}

	@Override
	public int compareTo(final Instrument o) {
		return id().compareTo(o.id());
	}

	@Override
	public boolean equals(final Object o) {

		if (!(o instanceof Instrument)) {
			return false;
		}

		return compareTo((Instrument) o) == 0;

	}

	@Override
	public int hashCode() {
		return id().hashCode();
	}

	/*
	 * Deprecated
	 */

	@Override
	public TimeInterval lifetime() {

		final Event start = calendar().event(Event.Type.FIRST_TRADE_DATE);
		final Event end = calendar().event(Event.Type.LAST_TRADE_DATE);

		if (start.isNull() && end.isNull()) {
			return TimeInterval.NULL;
		}

		return VALUES.newTimeInterval(
				start.isNull() ? 0 : start.date().getMillis(),
				end.isNull() ? 0 : end.date().getMillis());

	}

	@Override
	public Time contractExpire() {

		final Event event = calendar().event(Type.LAST_TRADE_DATE);

		if (event.isNull()) {
			return Time.NULL;
		}

		return VALUES.newTime(event.date().getMillis());

	}

	@Override
	public Month contractDeliveryMonth() {

		final Event event = calendar().event(Type.LAST_DELIVERY_DATE);

		if (event.isNull()) {
			return Month.NULL_MONTH;
		}

		switch (event.date().getMonthOfYear()) {

			case 1:
				return Month.JANUARY;
			case 2:
				return Month.FEBRUARY;
			case 3:
				return Month.MARCH;
			case 4:
				return Month.APRIL;
			case 5:
				return Month.MAY;
			case 6:
				return Month.JUNE;
			case 7:
				return Month.JULY;
			case 8:
				return Month.AUGUST;
			case 9:
				return Month.SEPTEMBER;
			case 10:
				return Month.OCTOBER;
			case 11:
				return Month.NOVEMEBR;
			case 12:
				return Month.DECEMBER;
			default:
				return Month.NULL_MONTH;

		}

	}

	@Override
	public String timeZoneName() {
		return timeZone().getID();
	}

	@Override
	public long timeZoneOffset() {
		return timeZone().getOffset(System.currentTimeMillis());
	}

	@Override
	public List<InstrumentID> componentLegs() {

		final List<InstrumentID> legs = new ArrayList<InstrumentID>();

		for (final SpreadLeg leg : spreadLegs())
			legs.add(leg.instrument());

		return legs;
	}


	@Override
	public String marketGUID() {
		return id().toString();
	}

	@Override
	public Fraction displayFraction() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Schedule marketHours() {
		return schedule();
	}

	@Override
	public Exchange exchange() {
		return Exchange.NULL;
	}

}
