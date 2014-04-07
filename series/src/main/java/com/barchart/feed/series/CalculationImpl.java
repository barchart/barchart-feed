package com.barchart.feed.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.series.Calculation;
import com.barchart.feed.api.series.Period;
import com.barchart.util.value.api.Decimal;

public class CalculationImpl extends DataPointImpl implements Calculation {
	private Decimal value;

	protected CalculationImpl(Period period, DateTime d, Decimal value) {
		super(period, d);

	}

	@Override
	public Decimal getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {

	}

}
