/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.provider;

import com.barchart.feed.base.values.api.TextValue;

final class NulText extends BaseText {

	static final String TEXT = "";

	@Override
	public final String toString() {
		return TEXT;
	}

	@Override
	public final TextValue toUpperCase() {
		return this;
	}

	@Override
	public final TextValue toLowerCase() {
		return this;
	}

}
