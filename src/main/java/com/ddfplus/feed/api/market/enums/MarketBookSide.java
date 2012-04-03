package com.ddfplus.feed.api.market.enums;

import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.Value;

public enum MarketBookSide implements Value<MarketBookSide> {

	/** price gap between bids and asks */
	GAP, //

	/** buy side */
	BID, //

	/** offer side */
	ASK, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookSide[] ENUM_VALUES = values();

	public static final MarketBookSide fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	static {
		// validate use of byte ordinal
		MathExtra.castIntToByte(ENUM_VALUES.length);
	}

	@Override
	public MarketBookSide freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == GAP;
	}

}
