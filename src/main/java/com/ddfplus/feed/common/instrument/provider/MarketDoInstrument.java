package com.ddfplus.feed.common.instrument.provider;

import com.barchart.util.anno.Mutable;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.instrument.enums.InstrumentField;
import com.ddfplus.feed.api.instrument.values.MarketInstrument;

@Mutable
public interface MarketDoInstrument extends MarketInstrument {

	<V extends Value<V>> void set(InstrumentField<V> field, V value);

}
