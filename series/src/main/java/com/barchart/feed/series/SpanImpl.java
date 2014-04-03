package com.barchart.feed.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.DataPoint;
import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.Span;

public class SpanImpl extends DataPointImpl implements Span {

	private DateTime nextDate;

	public static final SpanImpl INITIAL = new SpanImpl(
			new SpanImpl(Period.DAY,
					new DateTime(1980, 1, 1, 0, 0, 0),
					new DateTime(1980, 1, 1, 0, 0, 0))
			);

	public SpanImpl(final Period period, final DateTime d, final DateTime next) {
		super(period, d);
		this.nextDate = next;
	}

	public SpanImpl(final SpanImpl other) {
		super(new Period(other.period.getPeriodType(), other.period.size()), new DateTime(other.date));
		this.nextDate = new DateTime(other.nextDate.getMillis());
	}

	public void setSpan(final SpanImpl other) {
		this.period = new Period(other.period.getPeriodType(), other.period.size());
		this.date = new DateTime(other.date);
		this.nextDate = new DateTime(other.nextDate.getMillis());
	}

	@Override
	public <E extends DataPoint> int compareTo(final E other) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setNextDate(final DateTime dt) {
		this.nextDate = dt;
	}

	@Override
	public DateTime getNextDate() {
		return nextDate;
	}

	@Override
	public int getNextIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(date).append("  --  ").append(nextDate).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((nextDate == null) ? 0 : nextDate.hashCode());
		result = prime * result
				+ ((date == null) ? 0 : date.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SpanImpl other = (SpanImpl) obj;
		if (nextDate == null) {
			if (other.nextDate != null)
				return false;
		} else if (!period.getPeriodType().resolutionInstant(nextDate)
				.equals(period.getPeriodType().resolutionInstant(other.nextDate)))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!period.getPeriodType().resolutionInstant(date)
				.equals(period.getPeriodType().resolutionInstant(other.date)))
			return false;
		return true;
	}

	/**
	 * Returns true if the specified span intersects this span and has a start
	 * time less than this start time or a next time greater than this start
	 * time.
	 *
	 * @param span the span tested for extends quality
	 * @return true if so, false if not
	 */
	@Override
	public <T extends Span> boolean extendsSpan(final T span) {
		return span.intersection(this) != null && ((this.date.getMillis() < span.getDate().getMillis()) ||
				(this.nextDate.getMillis() > span.getNextDate().getMillis()));
	}

	/**
	 * Expands the lower and upper bounds of this {@code Span} to include the
	 * extremes of the specified Span if not already included.
	 *
	 * @param span
	 * @return the union of this Span and the specified Span
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Span> T union(final T span) {

		DateTime start = null;
		DateTime end = null;
		start = this.date.getMillis() < span.getDate().getMillis() ? this.date : span.getDate();
		end = this.nextDate.getMillis() < span.getNextDate().getMillis() ? span.getNextDate() : this.nextDate;

		return (T) new SpanImpl(span.getPeriod(), start, end);

	}

	/**
	 * Returns a {@code Span} whose range is the intersection of this
	 * {@code Span} and the specified Span.
	 *
	 * @param span the Span with which to combine to produce an intersection.
	 * @return a Span containing the interecting range of this Span and the Span
	 *         specified.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Span> T intersection(final T span) {

		if ((!(span.getDate().getMillis() < nextDate.getMillis())) &&
				(!(date.getMillis() < span.getNextDate().getMillis()))) {
			return null;
		}
		DateTime start = null;
		DateTime end = null;
		start = this.date.getMillis() < span.getDate().getMillis() ? span.getDate() : this.date;
		end = this.nextDate.getMillis() < span.getNextDate().getMillis() ? this.nextDate : span.getNextDate();

		return (T) new SpanImpl(span.getPeriod(), start, end);

	}

}
