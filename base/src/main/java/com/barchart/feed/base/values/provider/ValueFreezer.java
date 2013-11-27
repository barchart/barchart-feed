/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.Value;

/**
 * by default assume that "this" is already immutable
 */
public abstract class ValueFreezer<T extends Value<T>> implements
		Value<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T freeze() {
		return (T) this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
