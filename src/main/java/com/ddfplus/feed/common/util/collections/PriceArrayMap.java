package com.ddfplus.feed.common.util.collections;

import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.provider.ValueConst;

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
