/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.provider;

import static com.barchart.feed.api.enums.MarketSide.*;
import static com.barchart.feed.base.book.enums.MarketBookAction.*;
import static com.barchart.feed.base.provider.MarketConst.*;
import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.feed.api.data.PriceLevel;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketDoBookEntry;
import com.barchart.feed.base.book.enums.MarketBookAction;
import com.barchart.util.math.MathExtra;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.provider.ValueFreezer;

// JavaSize this = 8(obj) 4 * 1(byte) + 4(priceRef) + 4(sizeRef) = 24
public class DefBookEntry extends ValueFreezer<MarketBookEntry> implements
		MarketDoBookEntry {
	
	private static final Factory factory = FactoryLoader.load();

	private final static byte nulAct = NOOP.ord;
	private final static byte nulSide = GAP.ord;
	private final static byte nulType = BookLiquidityType.NONE.ord;

	// store byte ordinal to save heap
	private final byte ordAct;
	private final byte ordSide;
	private final byte ordType;

	// place expected to fit byte size
	private final byte place;

	// will restore from null
	private final PriceValue price;
	private final SizeValue size;

	public DefBookEntry(final MarketBookAction act, final MarketSide side,
			final BookLiquidityType type, final int place, final PriceValue price,
			final SizeValue size) throws ArithmeticException {

		this.ordAct = (act == null ? nulAct : act.ord);
		this.ordSide = (side == null ? nulSide : side.ord);
		this.ordType = (type == null ? nulType : type.ord);

		this.place = MathExtra.castIntToByte(place);

		this.price = price;
		this.size = size;

	}

	@Override
	public final MarketBookAction act() {
		return MarketBookAction.fromOrd(ordAct);
	}

	@Override
	public final MarketSide side() {
		return MarketSide.fromOrd(ordSide);
	}

	@Override
	public final BookLiquidityType type() {
		return BookLiquidityType.fromOrd(ordType);
	}

	@Override
	public final int place() {
		return place;
	}

	@Override
	public final PriceValue priceValue() {
		return (price == null) ? NULL_PRICE : price;
	}

	@Override
	public Price price() {
		
		if(price == null) {
			return ValueConst.NULL_PRICE;
		} else {
			return factory.newPrice(price.mantissa(), price.exponent());
		}
	}

	@Override
	public final SizeValue sizeValue() {
		return (size == null) ? NULL_SIZE : size;
	}
	
	@Override
	public Size size() {
		
		if(size == null) {
			return ValueConst.NULL_SIZE;
		} else {
			return factory.newSize(size.asLong(), 0);
		}
	}

	//

	@Override
	public String toString() {
		return String.format("%s   %s   %s   %s", side(), place(), priceValue(),
				sizeValue());
	}

	public String toStringFull() {
		return String.format("%s   %s   %s   %s   %s   %s", act(), side(),
				type(), place(), priceValue(), sizeValue());
	}

	private static final void checkNull(final Object value) {
		if (value == null) {
			throw new NullPointerException("invalid implementation");
		}
	}

	static {
		final DefBookEntry entry = new DefBookEntry(null, null, null, 0, null,
				null);
		checkNull(entry.act());
		checkNull(entry.side());
		checkNull(entry.type());
		checkNull(entry.priceValue());
		checkNull(entry.sizeValue());
	}

	@Override
	public final int hashCode() {
		return ((ordAct << 24) | (ordSide << 16) | (ordType << 8))
				^ (priceValue().hashCode()) ^ (sizeValue().hashCode());
	}

	@Override
	public final boolean equals(final Object thatEntry) {
		if (thatEntry instanceof DefBookEntry) {
			final DefBookEntry that = (DefBookEntry) thatEntry;
			return this.ordAct == that.ordAct && this.ordSide == that.ordSide
					&& this.ordType == that.ordType && this.place == that.place
					&& this.priceValue().equals(that.priceValue())
					&& this.sizeValue().equals(that.sizeValue());
		}
		return false;
	}

	@Override
	public final boolean isNull() {
		return this == NULL_BOOK_ENTRY;
	}

	@Override
	public int level() {
		return place;
	}

	@Override
	public Time lastUpdateTime() {
		return null; //TODO
	}

	@Override
	public PriceLevel copy() {
		return this.freeze();
	}

}
