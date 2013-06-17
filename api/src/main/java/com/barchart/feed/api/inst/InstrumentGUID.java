/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.api.inst;

public final class InstrumentGUID implements Comparable<InstrumentGUID>, 
		CharSequence {

	private final CharSequence guid;
	
	public InstrumentGUID(final CharSequence guid) {
		this.guid = guid;
	}
	
	@Override
	public int compareTo(final InstrumentGUID thatGUID) {
		final CharSequence that = thatGUID.guid;
		final int s1 = this.length();
		final int s2 = that.length();
		final int size = Math.min(s1, s2);
		for (int k = 1; k < size; k++) {
			final int c1 = this.charAt(k);
			final int c2 = that.charAt(k);
			if (c1 == c2) {
				continue;
			} else {
				return c1 - c2;
			}
		}
		return s1 - s2;
	}

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
			if(compareTo((InstrumentGUID) o) == 0) {
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
	
	@Override
	public String toString() {
		return guid.toString();
	}
	
	/** null instrument GUID */
	public static final InstrumentGUID NULL_INSTRUMENT_GUID = new InstrumentGUID("NULL"); 

}
