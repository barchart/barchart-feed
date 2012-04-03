package com.ddfplus.feed.api.instrument.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.InstrumentField;

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
