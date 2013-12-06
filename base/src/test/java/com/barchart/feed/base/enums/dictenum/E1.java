/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;

import com.barchart.feed.base.enums.DictEnum;

public class E1<V> extends DictEnum<V> {

	// public static E1[] values() {
	// return DictEnum.valuesFor(E1.class);
	// }

	protected E1() {
	}

	protected E1(final String comment) {
		super(comment);
	}

	private final static <X> E1<X> NEW(final String comment) {
		return new E1<X>(comment);
	}

	public static final E1<String> A0 = NEW("E1-0");
	public static final E1<String> A1 = NEW("E1-1");
	public static final E1<String> A2 = NEW("E1-2");

}
