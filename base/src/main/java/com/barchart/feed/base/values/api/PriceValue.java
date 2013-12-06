/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.api;

import com.barchart.feed.base.values.lang.ScaledDecimal;
import com.barchart.util.common.anno.NotMutable;

/** should be used for prices only */
@NotMutable
public interface PriceValue extends Value<PriceValue>,
		ScaledDecimal<PriceValue, DecimalValue> {

	double asDouble();
	
}
