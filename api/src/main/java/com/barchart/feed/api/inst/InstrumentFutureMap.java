package com.barchart.feed.api.inst;

import java.util.List;
import java.util.Map;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.concurrent.FutureNotifierBase;

public class InstrumentFutureMap<V> extends FutureNotifierBase<Map<V, List<Instrument>>, 
		InstrumentFutureMap<V>> {

}
