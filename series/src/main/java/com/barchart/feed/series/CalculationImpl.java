package com.barchart.feed.series;

import com.barchart.feed.api.series.Calculation;
import com.barchart.feed.api.series.Period;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Time;

public class CalculationImpl extends DataPointImpl implements Calculation {
	private Decimal value;
	
	protected CalculationImpl(Period period, Time t, Decimal value) {
		super(period, t);
		
	}

	@Override
	public Decimal getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		
	}

}
