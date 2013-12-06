/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums.dictenum;


public class E121<V> extends E12<V> {

	// public static E121<?>[] values() {
	// return DictEnum.valuesFor(E121.class);
	// }

	private E121() {
	}

	private E121(final String comment) {
		super(comment);
	}

	private final static <X> E121<X> NEW(final String comment) {
		return new E121<X>(comment);
	}

	public static final E121<String> E0 = NEW("E121-0");
	public static final E121<String> E1 = NEW("E121-1");
	public static final E121<String> E3 = NEW("E121-2");

}
