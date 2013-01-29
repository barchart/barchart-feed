package com.barchart.feed.inst.provider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.api.InstrumentService;

public class LocalDBInstrumentService implements InstrumentService<InstrumentGUID> {
	
	private static final String DBLocation = "src/test/resources/bdb";
	
	final LocalInstDefDB instDefDB;
	
	private final ConcurrentMap<InstrumentGUID, Instrument> instMap = 
			new ConcurrentHashMap<InstrumentGUID, Instrument>();
	private final ConcurrentMap<CharSequence, InstrumentGUID> guidMap =
			new ConcurrentHashMap<CharSequence, InstrumentGUID>();
	private final ConcurrentMap<CharSequence, InstrumentGUID> failedLookupMap =
			new ConcurrentHashMap<CharSequence, InstrumentGUID>();
	
	public LocalDBInstrumentService(final String location) {
		
		if(location == null || location.length() == 0) {
			throw new IllegalArgumentException("Invalid location");
		}
		
		instDefDB = new LocalInstDefDB(location);
    	
	}

	@Override
	public Instrument lookup(InstrumentGUID symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Instrument> lookupAsync(InstrumentGUID symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<InstrumentGUID, Instrument> lookup(List<InstrumentGUID> symbols) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<InstrumentGUID, Future<Instrument>> lookupAsync(
			List<InstrumentGUID> symbols) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Instrument lookup(final CharSequence symbol) {
//		
//		InstrumentGUID guid = guidMap.get(symbol);
//		
//		if(guid != null) {
//			
//			guid = failedLookupMap.get(symbol);
//			if(guid != null) {
//				return InstrumentConst.NULL_INSTRUMENT;
//			}
//			
//		}
//		
//		Instrument inst;
//		inst = instMap.get(guid);
//		
//		if(inst != null) {
//			return inst;
//		}
//		
//		inst = instDefDB.lookup(guid);
//		if(inst != null) {
//			instMap.put(guid, inst);
//			return inst;
//		}
//		
//		return InstrumentConst.NULL_INSTRUMENT;
//	}
	

}
