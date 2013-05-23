/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.map.provider;

import java.util.Map;

import com.barchart.util.collections.PriceArrayMap;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueConst;

public class DefCuvolMap extends NulCuvolMap {

	public static double EPSILON = 0.0000000001;
	
	private final Map<PriceValue, SizeValue> offGridMap;
	
	private final SizeValue[] entries;
	private final PriceArrayMap<SizeValue> map;

	private final PriceValue priceFirst;
	private final PriceValue priceStep;
	
	DefCuvolMap(final Map<PriceValue, SizeValue> offGridMap, PriceArrayMap<SizeValue> map, 
			final PriceValue priceFirst, final PriceValue priceStep) {

		assert offGridMap != null;
		assert priceFirst != null;
		assert priceStep != null;
		assert priceStep.mantissa() != 0;

		this.offGridMap = offGridMap;
		
		this.priceFirst = priceFirst;
		this.priceStep = priceStep;

		this.map = map;
		
		entries = entries();

		for (int k = 0; k < entries.length; k++) {
			SizeValue volume = entries[k];
			if (volume == null) {
				volume = ValueConst.NULL_SIZE;
			}
			entries[k] = volume.freeze();
		}
		
	}
	
	@Override
	public SizeValue getCuvol(final PriceValue price) {
		
		SizeValue size = ValueConst.NULL_SIZE;
		if(onGrid(price)) {
			size = map.get(price);
		} else if(offGridMap != null) {
			size = offGridMap.get(price);
		}
		
		if(size == null) {
			return ValueConst.NULL_SIZE;
		} else {
			return size;
		}
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
	
	private boolean onGrid(final PriceValue price) {
		
		//Do without double
		final double result = price.asDouble() / priceStep.asDouble();
		
		final long resLong = Math.round(result);
		
		if(Math.abs(result - resLong) < EPSILON) {
			return true;
		}
		
		return false;
		
	}
	
}
