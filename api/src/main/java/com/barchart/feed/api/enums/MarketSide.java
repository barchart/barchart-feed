package com.barchart.feed.api.enums;

public enum MarketSide {
	
	GAP, BID, ASK;
	
	public final byte ord = (byte) ordinal();
	
	private static final MarketSide[] ENUM_VALUES = values();
	
	public static final MarketSide fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

}
