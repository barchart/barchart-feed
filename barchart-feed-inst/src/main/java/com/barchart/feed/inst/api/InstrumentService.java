package com.barchart.feed.inst.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface InstrumentService<V> {
	
	Instrument lookup(V symbol);
	
	Future<Instrument> lookupAsync(V symbol);
	
	Map<V, Instrument> lookup(List<V> symbols);
	
	Map<V, Future<Instrument>> lookupAsync(List<V> symbols);
	
}
