/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.cuvol.map.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.cuvol.map.api.MarketDoCuvolMap;
import com.barchart.feed.base.provider.DefCuvolEntry;
import com.barchart.feed.base.provider.MarketConst;
import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.collections.PriceArrayMap;
import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueBuilder;
import com.barchart.util.values.provider.ValueConst;
import com.barchart.util.values.util.ValueUtil;

@Mutable
@NotThreadSafe
public class VarCuvolMap extends NulCuvolMap implements MarketDoCuvolMap {
	
	public static double EPSILON = 0.0000000001;
	public static int ERROR_INDEX = -1;

	private volatile SortedMap<PriceValue, SizeValue> offGridMap = null;
	
	private final PriceArrayMap<SizeValue> map;
	
	private final PriceValue priceStep;
	private final DecimalValue decPriceStep;
	private PriceValue priceLast;
	
	public VarCuvolMap(final PriceValue priceStep) {
		this.priceStep = priceStep;
		decPriceStep = ValueBuilder.newDecimal(priceStep.mantissa(), priceStep.exponent());
		this.map = new PriceArrayMap<SizeValue>(priceStep);
	}
	
	@Override
	public void add(PriceValue price, SizeValue size) {
		
		assert price != null;
		assert size != null;
		
		if(onGrid(price)) {
			
			SizeValue volume = map.get(price);

			if (volume == null) {
				volume = size;
			} else {
				volume = volume.add(size);
			}

			map.put(price, volume);

			priceLast = price;
			return;
		} 
		
		/* One time build if needed */
		if(offGridMap == null) {
			offGridMap = new TreeMap<PriceValue, SizeValue>();
		}
		
		SizeValue volume = offGridMap.get(price);

		if (volume == null) {
			volume = size;
		} else {
			volume = volume.add(size);
		}

		offGridMap.put(price, volume);
		/*  */
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
	public final DefCuvolMap freeze() {
		
		final Map<PriceValue, SizeValue> frozen = new HashMap<PriceValue, SizeValue>();
		
		if(offGridMap != null) {	
			for(final Entry<PriceValue, SizeValue> e : offGridMap.entrySet()) {
				frozen.put(e.getKey().freeze(), e.getValue().freeze());
			}
		}
		
		return new DefCuvolMap(frozen, map, priceFirst(), priceStep());
		
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}
	
	@Override 
	public final PriceValue priceFirst() {
		return map.keyHead();
	}
	
	@Override
	public final PriceValue priceStep() {
		return priceStep;
	}
	
	@Override
	public MarketCuvolEntry getLastEntry() {
		
		final PriceValue price = priceLast;

		if (price == null) {
			return MarketConst.NULL_CUVOL_ENTRY;
		}

		final SizeValue size = map.get(price);

		if (size == null) {
			return MarketConst.NULL_CUVOL_ENTRY;
		}

		final int index = map.getIndex(price);

		final MarketCuvolEntry entry = new DefCuvolEntry(index, price, size);

		return entry;
		
	}

	@Override
	public void clear() {
		offGridMap.clear();
	}

	private boolean onGrid(final PriceValue price) {
		
		if(price.div(decPriceStep).norm().exponent() >= 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static void main(final String[] args) {
		
		DecimalValue priceStep = ValueBuilder.newDecimal(25, -2);
		
		PriceValue test1 = ValueBuilder.newPrice(1000);
		PriceValue test2 = ValueBuilder.newPrice(100);
		PriceValue test3 = ValueBuilder.newPrice(10);
		PriceValue test4 = ValueBuilder.newPrice(1);
		PriceValue test5 = ValueBuilder.newPrice(.5);
		PriceValue test6 = ValueBuilder.newPrice(.25);
		PriceValue test7 = ValueBuilder.newPrice(10000.1);
		PriceValue test8 = ValueBuilder.newPrice(1.26);
		PriceValue test9 = ValueBuilder.newPrice(1.0001);
		PriceValue test10 = ValueBuilder.newPrice(1.000005);
		
		System.out.println(onGrid(test1, priceStep));
		System.out.println(onGrid(test2, priceStep));
		System.out.println(onGrid(test3, priceStep));
		System.out.println(onGrid(test4, priceStep));
		System.out.println(onGrid(test5, priceStep));
		System.out.println(onGrid(test6, priceStep));
		System.out.println(onGrid(test7, priceStep));
		System.out.println(onGrid(test8, priceStep));
		System.out.println(onGrid(test9, priceStep));
		System.out.println(onGrid(test10, priceStep));
		
	}
	
	private static boolean onGrid(final PriceValue price, final DecimalValue priceStep) {
		
		if(price.div(priceStep).norm().exponent() >= 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
	
	
	
	
	
	
}
