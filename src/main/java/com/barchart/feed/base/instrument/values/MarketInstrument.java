/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.values;

import com.barchart.feed.base.instrument.enums.InstrumentField;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;

/** represents generic instrument */
@NotMutable
public interface MarketInstrument extends Value<MarketInstrument>,
		Comparable<MarketInstrument> {

	<V extends Value<V>> V get(InstrumentField<V> field);

	//

	/** since used as map key */
	@Override
	boolean equals(Object thatInst);

	/** since used as map key */
	@Override
	int hashCode();

	/** since used for sorting */
	@Override
	int compareTo(MarketInstrument thatInst);

}
