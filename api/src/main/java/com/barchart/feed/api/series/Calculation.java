package com.barchart.feed.api.series;

import com.barchart.util.value.api.Decimal;

public interface Calculation extends DataPoint {
	/**
	 * Returns the two-dimensional value of this {@code Calculation}
	 * @return the value of this Calculation
	 */
	public Decimal getValue();
	
	/**
	 * Sets the two-dimensional value of this {@code Calculation}
	 * @param the value of this Calculation
	 */
	public void setValue(double value);
}
