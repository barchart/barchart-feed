package com.barchart.feed.inst.api;

import java.util.List;
import java.util.concurrent.FutureTask;

public interface InstrumentService {
	
	Instrument lookup(CharSequence symbol);
	
	FutureTask<Instrument> lookupAsync(CharSequence symbol);
	
	List<Instrument> lookup(List<CharSequence> symbols);
	
	FutureTask<List<Instrument>> lookupAsync(List<CharSequence> symbols);
	
	
	Instrument lookup(InstrumentGUID guid);
	
	FutureTask<Instrument> lookupAsnyc(InstrumentGUID guid);
	
	

}
