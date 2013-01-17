package com.barchart.feed.inst.api;

import java.util.List;


public interface SymbologyContext {

	InstrumentGUID lookup(CharSequence symbol);
	
	List<InstrumentGUID> search(CharSequence symbol);
	
	List<InstrumentGUID> search(CharSequence symbol, int limit, int offset);
	
}
