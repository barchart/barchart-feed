/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.inst;

import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.provider.ValueBuilder;

public final class InstrumentGUID implements Comparable<InstrumentGUID>, Value<InstrumentGUID>, CharSequence {

	private final TextValue guid;
	
	public InstrumentGUID(final TextValue guid) {
		this.guid = guid;
	}
	
	public InstrumentGUID(final String seq) {
		this.guid = ValueBuilder.newText(seq);
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
	public int hashCode() {
		return guid.hashCode();
	}
	
	
	/** null instrument */
	public static final InstrumentGUID NULL_INSTRUMENT_GUID = new InstrumentGUID("NULL"); 

}
