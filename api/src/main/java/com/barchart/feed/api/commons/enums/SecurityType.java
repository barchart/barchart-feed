/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.commons.enums;

import com.barchart.util.values.api.Value;

public enum SecurityType implements Value<SecurityType> {

	NULL_TYPE,
	FOREX,
	INDEX,
	EQUITY,
	FUTURE,
	OPTION,
	SPREAD;

	@Override
	public SecurityType freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_TYPE;
	}
	
}
