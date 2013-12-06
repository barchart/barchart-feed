/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.bench;

import com.barchart.feed.base.enums.ParamEnumBase;

class EnumForValue<V extends ValueIce> extends
		ParamEnumBase<V, EnumForValue<V>> {

	// keep first
	private static int sequence;
	protected static final int size;
	protected static final EnumForValue<?>[] values;
	static {
		sequence = 0;
		size = countEnumFields(EnumForValue.class);
		values = new EnumForValue<?>[size];
	}

	public static final int size() {
		return size;
	}

	public static final EnumForValue<?>[] values() {
		return values.clone();
	}

	//

	protected EnumForValue(V defVal) {
		super(values, sequence++, defVal);
	}

	protected EnumForValue(EnumForValue<?>[] values, int sequence, V defVal) {
		super(values, sequence++, defVal);
	}

	//

	public static final EnumForValue<ValueOne> VALUE_ONE = //
	new EnumForValue<ValueOne>(null);

	public static final EnumForValue<ValueTwo> VALUE_TWO = //
	new EnumForValue<ValueTwo>(null);

}
