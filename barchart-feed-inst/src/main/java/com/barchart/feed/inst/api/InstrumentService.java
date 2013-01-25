package com.barchart.feed.inst.api;

import java.util.List;
import java.util.concurrent.Future;

public interface InstrumentService {
	
	Instrument lookup(CharSequence symbol);
	
	Future<Instrument> lookupAsync(CharSequence symbol);
	
	List<Instrument> lookup(List<CharSequence> symbols);
	
	Future<List<Instrument>> lookupAsync(List<CharSequence> symbols);
	
	Instrument lookup(InstrumentGUID guid);
	
	Future<Instrument> lookupAsync(InstrumentGUID guid);
	
}
