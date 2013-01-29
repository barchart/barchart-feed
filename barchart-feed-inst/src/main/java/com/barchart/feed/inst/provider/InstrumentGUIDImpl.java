package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.InstrumentGUID;

public class InstrumentGUIDImpl implements InstrumentGUID {

	private final long guid;
	
	public InstrumentGUIDImpl(final long guid) {
		this.guid = guid;
	}
	
	@Override
	public long getGUID() {
		return guid;
	}

	@Override 
	public boolean equals(final Object o) {
		
		if(o == null) {
			return false;
		}
		
		if(o instanceof InstrumentGUID) {
			if(guid == ((InstrumentGUID)o).getGUID()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	@Override
	public int compareTo(final InstrumentGUID thatGUID) {
		
		if(guid > thatGUID.getGUID()) {
			return 1;
		} else if(guid < thatGUID.getGUID()) {
			return -1;
		} else {
			return 0;
		}
	}

}
