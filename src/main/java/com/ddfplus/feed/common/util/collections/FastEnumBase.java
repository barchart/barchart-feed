package com.ddfplus.feed.common.util.collections;

class FastEnumBase<T extends BitSetEnum<T>> {

	protected final T[] enumValues;

	protected FastEnumBase(final T[] values) {
		enumValues = values;
		if (values.length > BitSetEnum.LIMIT) {
			throw new IllegalArgumentException("invalid use of : "
					+ FastEnumBase.class.getName() + " with : "
					+ values.getClass().getName() + " ; termintating");
		}
	}

}
