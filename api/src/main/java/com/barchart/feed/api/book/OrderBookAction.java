package com.barchart.feed.api.book;

public enum OrderBookAction {
	
	/** no operation */
	NOOP, //

	/** add, new, change, overlay, modify */
	MODIFY, //

	/** clear, delete, remove */
	REMOVE, //

	;

	public final byte ord = (byte) ordinal();
	
	private static final OrderBookAction[] ENUM_VALUES = values();
	
	public static final OrderBookAction fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}
	
}
