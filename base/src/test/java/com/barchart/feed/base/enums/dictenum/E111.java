/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;


public class E111<V> extends E11<V> {

	// public static E111<?>[] values() {
	// return DictEnum.valuesFor(E111.class);
	// }

	protected E111() {
	}

	protected E111(final String comment) {
		super(comment);
	}

	private final static <X> E111<X> NEW(final String comment) {
		return new E111<X>(comment);
	}

	public static final E111<String> C0 = NEW("E111-0");
	public static final E111<String> C1 = NEW("E111-1");
	public static final E111<String> C2 = NEW("E111-2");

}
