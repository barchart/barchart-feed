package com.barchart.feed.base.cuvol.map.provider;

import java.util.Map;

import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

public class DefCuvolMap extends NulCuvolMap {

	private final Map<PriceValue, SizeValue> map;
	
	private final SizeValue[] entries;

	private final PriceValue priceFirst;
	private final PriceValue priceStep;
	
	DefCuvolMap(final Map<PriceValue, SizeValue> offGridMap, final SizeValue[] map, 
			final PriceValue priceFirst, final PriceValue priceStep) {

		assert offGridMap != null;
		assert priceFirst != null;
		assert priceStep != null;
		assert priceStep.mantissa() != 0;

		this.map = offGridMap;
		
		this.priceFirst = priceFirst;
		this.priceStep = priceStep;

		this.entries = map;
		
	}
	
	@Override
	public SizeValue getCuvol(final PriceValue price) {
		return map.get(price);
	}
	
	@Override
	public PriceValue priceFirst() {
		return priceFirst;
	}

	@Override
	public PriceValue priceStep() {
		return priceStep;
	}

	@Override
	public SizeValue[] entries() {
		return entries;
	}
	
}
