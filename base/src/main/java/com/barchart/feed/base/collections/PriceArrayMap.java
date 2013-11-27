/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.collections;

import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.provider.ValueConst;
import com.barchart.util.anno.NotThreadSafe;

@NotThreadSafe
public class PriceArrayMap<V> extends ScadecArrayMapWriteable<PriceValue, V> {

	protected V[] valueArray;

	protected PriceValue keyHead;
	protected PriceValue keyTail;

	//

	public PriceArrayMap(final PriceValue pricePoint) {

		this(pricePoint, DEFAULT_LIMIT);

	}

	public PriceArrayMap(final PriceValue pricePoint, final int limit) {

		super(pricePoint, limit);

		clear();

	}

	@Override
	public final PriceValue keyHead() {
		return keyHead;
	}

	@Override
	public final PriceValue keyTail() {
		return keyTail;
	}

	@Override
	protected final V[] getArray() {
		return valueArray;
	}

	@Override
	protected final void setArray(V[] array) {
		valueArray = array;
	}

	@Override
	protected final void keyHead(PriceValue price) {
		keyHead = price;
	}

	@Override
	protected final void keyTail(PriceValue price) {
		keyTail = price;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void clear() {
		setArray((V[]) new Object[0]);
		keyHead(ValueConst.NULL_PRICE);
		keyTail(ValueConst.NULL_PRICE);
	}

}
