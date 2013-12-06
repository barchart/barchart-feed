/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;


public class E11<V> extends E1<V> {

	// public static E11<?>[] values() {
	// return DictEnum.valuesFor(E11.class);
	// }

	protected E11() {
	}

	protected E11(final String comment) {
		super(comment);
	}

	private final static <X> E11<X> NEW(final String comment) {
		return new E11<X>(comment);
	}

	public static final E11<String> A_0 = NEW("E11-0");
	public static final E11<String> A_1 = NEW("E11-1");
	public static final E11<String> A_2 = NEW("E11-2");

	public static final E11<String> B_0 = NEW("E11-3");
	public static final E11<String> B_1 = NEW("E11-4");
	public static final E11<String> B_2 = NEW("E11-5");

}
