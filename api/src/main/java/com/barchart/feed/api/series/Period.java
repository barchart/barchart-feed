package com.barchart.feed.api.series;


/**
 * A combination of a {@link PeriodType} and a duration, which together
 * specify the unit of time contained within a single {@link TimePoint}.
 * 
 * @author Jeremy
 * @author David Ray
 */
public final class Period {

	public static final Period ONE_MINUTE = new Period(
			PeriodType.MINUTE, 1);
	public static final Period FIVE_MINUTE = new Period(
			PeriodType.MINUTE, 5);
	public static final Period FIFTEEN_MINUTE = new Period(
			PeriodType.MINUTE, 15);
	public static final Period THIRTY_MINUTE = new Period(
			PeriodType.MINUTE, 30);
	public static final Period ONE_HOUR = new Period(
			PeriodType.HOUR, 1);
	
	public static final Period DAY = new Period(PeriodType.DAY, 1);
	public static final Period WEEK = new Period(PeriodType.WEEK, 1);
	public static final Period MONTH = new Period(PeriodType.MONTH, 1);
	public static final Period QUARTER = new Period(PeriodType.QUARTER, 1);
	public static final Period SIX_MONTH = new Period(PeriodType.MONTH, 6);
	public static final Period YEAR = new Period(PeriodType.YEAR, 1);

	
	private final PeriodType periodType;
	private final int size;

	public Period(final PeriodType periodType, final int size) {
		this.periodType = periodType;
		this.size = size;
	}

	/**
	 * The {@code Period} {@link PeriodType}
	 */
	public PeriodType getPeriodType() {
		return periodType;
	}
	
	/**
	 * Returns a flag indicating whether the specified comparison Period is 
	 * closer to the target Period than this Period.
	 * 
	 * @param target       the Period to measure the distance from
	 * @param comparison   the Period competing with this Period.
	 * @return     true if so, false if not
	 */
	public boolean isCloserTo(Period target, Period comparison) {
	    if(Math.abs(periodType.distance(target.getPeriodType())) <
	        Math.abs(comparison.getPeriodType().distance(target.getPeriodType()))) {
	        return true;
	    }
	    
	    return Math.abs(size - target.size()) < Math.abs(comparison.size() - target.size());
	}
	
	/**
	 * Returns a flag indicating whether the specified {@code Period} is
	 * higher than this one. A Period is higher when its {@link PeriodType}
	 * is higher <em>or</em> its PeriodType is the same but its size is larger.
	 * @param p
	 * @return true if so, false if not
	 */
	public boolean isHigherThan(Period p) {
	    return periodType.isHigherThan(p.getPeriodType()) ||
	        (periodType == p.periodType && size > p.size);
	}
	
	/**
     * Returns a flag indicating whether the specified {@code Period} is
     * lower than this one. A Period is lower when its {@link PeriodType}
     * is lower <em>or</em> its PeriodType is the same but its size is smaller.
     * @param p
     * @return  true if so, false if not
     */
	public boolean isLowerThan(Period p) {
        return periodType.isLowerThan(p.getPeriodType()) ||
            (periodType == p.periodType && size < p.size);
    }

	/**
	 * The number of {@code Period}s contained in one bar
	 */
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((periodType == null) ? 0 : periodType.hashCode());
		result = prime * result + size;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (periodType != other.periodType)
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public String toString() {
	    return new StringBuilder(periodType.toString()).append("(").append(size).append(")").toString();
	}
}
