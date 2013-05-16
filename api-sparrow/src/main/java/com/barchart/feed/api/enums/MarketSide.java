package com.barchart.feed.api.enums;

public enum MarketSide {

	BID, //

	ASK, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketSide[] VALUES = values();

	public static final MarketSide fromOrd(final byte ord) {
		return VALUES[ord];
	}

}
