package com.ddfplus.feed.api.market.enums;

import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.Value;

public enum MarketBookAction implements Value<MarketBookAction> {

	/** no operation */
	NOOP, //

	/** add, new, change, overlay, modify */
	MODIFY, //

	/** clear, delete, remove */
	REMOVE, //

	;

	public final byte ord = (byte) ordinal();

	private static final MarketBookAction[] ENUM_VALUES = values();

	public static final MarketBookAction fromOrd(final byte ord) {
		return ENUM_VALUES[ord];
	}

	static {
		// validate use of byte ordinal
		MathExtra.castIntToByte(ENUM_VALUES.length);
	}

	@Override
	public MarketBookAction freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NOOP;
	}

}
