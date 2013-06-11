package com.barchart.feed.api.inst;

import java.util.Map;

import com.barchart.feed.api.data.Instrument;
import com.barchart.util.concurrent.FutureNotifierBase;

public class InstrumentFutureMap<V> extends FutureNotifierBase<Map<V, Instrument>, 
		InstrumentFutureMap<V>> {

}
