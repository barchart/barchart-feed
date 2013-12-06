/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

public class ParaEnumBase<V, T extends ParamEnum<V, T>> extends DictEnum<V>
		implements ParamEnum<V, T> {

	{
		// System.out.println("LOAD ParaEnumBase");
	}

	protected ParaEnumBase() {
		super();
		this.value = null;
	}

	protected ParaEnumBase(final String comment, final V value) {
		super(comment);
		this.value = value;
	}

	private final V value;

	@Override
	public final V value() {
		return value;
	}

	@Override
	public final boolean is(final ParamEnum<?, ?> that) {
		/* assuming same class loader */
		return this == that;
	}

	@Override
	public final boolean isIn(final ParamEnum<?, ?>... thatArray) {
		if (thatArray == null) {
			return false;
		}
		for (final ParamEnum<?, ?> that : thatArray) {
			if (is(that)) {
				return true;
			}
		}
		return false;
	}

	// @Override
	// public int compareTo(final Dict<?> that) {
	// if (that instanceof DictEnum) {
	// final DictEnum<?> thisDict = this;
	// final DictEnum<?> thatDict = (DictEnum<?>) that;
	// return thisDict.compareTo(thatDict);
	// }
	// return 0;
	// }

}
