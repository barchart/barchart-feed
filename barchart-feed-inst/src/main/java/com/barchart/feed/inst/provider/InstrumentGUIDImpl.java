package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.ValueBuilder;

public class InstrumentGUIDImpl implements InstrumentGUID {

	private final TextValue guid;
	
	public InstrumentGUIDImpl(final TextValue guid) {
		this.guid = guid;
	}
	
	public InstrumentGUIDImpl(final String seq) {
		guid = ValueBuilder.newText(seq);
	}
	
	@Override 
	public boolean equals(final Object o) {
		
		if(o == null) {
			return false;
		}
		
		if(o instanceof InstrumentGUID) {
			if(guid.equals(o)) {
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
		return guid.compareTo(thatGUID);
	}

	@Override
	public InstrumentGUID freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_INSTRUMENT_GUID;
	}

	@Override
	public int length() {
		return guid.length();
	}

	@Override
	public char charAt(int index) {
		return guid.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return guid.subSequence(start, end);
	}

}
