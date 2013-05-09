package com.barchart.feed.api.data.temp;

/**
 * Suggested new value class for prices, will be a copy of ScaledDecimal.
 */
public interface PriceValue {
	
	public boolean greaterThan(PriceValue that);
	public boolean lessThan(PriceValue that);

}
