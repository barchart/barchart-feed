package com.barchart.feed.api.inst;

import java.util.Map;

import com.barchart.feed.api.data.Instrument;
import com.barchart.util.concurrent.ListenableFuture;

public interface InstrumentFutureMap<V> extends 
		ListenableFuture<Map<V, Instrument>, InstrumentFutureMap<V>> {

}
