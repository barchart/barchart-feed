/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.json;

import com.barchart.feed.base.values.api.DecimalValue;
import com.barchart.feed.base.values.api.PriceValue;

class PriceValueSer extends ScaledValueSer<PriceValue, DecimalValue> {

	protected PriceValueSer(Class<PriceValue> klaz) {
		super(klaz);
	}

}
