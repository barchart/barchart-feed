package com.barchart.feed.inst.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface SymbologyContext<V> {

	InstrumentGUID lookup(V symbol);
	
	Map<V, InstrumentGUID> lookup(Collection<? extends V> symbols);
	
	List<InstrumentGUID> search(CharSequence symbol);
	
	List<InstrumentGUID> search(CharSequence symbol, int limit, int offset);
	
}
