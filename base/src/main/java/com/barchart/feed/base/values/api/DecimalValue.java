/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.api;

import com.barchart.feed.base.values.lang.ScaledDecimal;
import com.barchart.util.anno.NotMutable;

/** generic decimal value */
@NotMutable
public interface DecimalValue extends Value<DecimalValue>,
		ScaledDecimal<DecimalValue, DecimalValue> {

	double asDouble();
	
}
