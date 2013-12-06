/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;


public class E12<V> extends E1<V> {

	// public static E12<?>[] values() {
	// return DictEnum.valuesFor(E12.class);
	// }

	protected E12() {
	}

	protected E12(final String comment) {
		super(comment);
	}

	private final static <X> E12<X> NEW(final String comment) {
		return new E12<X>(comment);
	}

	public static final E12<String> D0 = NEW("E12-0");
	public static final E12<String> D1 = NEW("E12-1");
	public static final E12<String> D2 = NEW("E12-2");

}
