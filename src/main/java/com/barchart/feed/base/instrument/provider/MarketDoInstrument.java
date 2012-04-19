/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.provider;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.Value;

@Mutable
public interface MarketDoInstrument extends MarketInstrument {

	<V extends Value<V>> void set(InstrumentField<V> field, V value);

}
