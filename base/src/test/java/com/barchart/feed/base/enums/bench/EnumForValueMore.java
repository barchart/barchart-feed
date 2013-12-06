/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.bench;

// probably not a good idea?

class EnumForValueMore<V extends ValueIce> extends EnumForValue<V> {

	// keep first
	private static int sequence;
	private static final int size;
	private static final EnumForValue<?>[] values;
	static {
		sequence = EnumForValue.size;
		size = countEnumFields(EnumForValueMore.class) + EnumForValue.size;
		values = new EnumForValue<?>[size]; // TODO add copy from superclass
	}

	protected EnumForValueMore(V defVal) {
		super(values, sequence++, defVal);
	}

	//

	public static final EnumForValue<ValueZen> VALUE_ZEN = //
	new EnumForValue<ValueZen>(null);

}
