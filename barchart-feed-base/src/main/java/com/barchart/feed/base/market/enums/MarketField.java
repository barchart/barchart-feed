/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market.enums;

import static com.barchart.feed.base.provider.MarketConst.*;
import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBook;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.cuvol.api.MarketCuvol;
import com.barchart.feed.base.cuvol.api.MarketCuvolEntry;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.state.api.MarketState;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.util.anno.NotMutable;
import com.barchart.util.collections.BitSetEnum;
import com.barchart.util.enums.DictEnum;
import com.barchart.util.enums.ParaEnumBase;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;

@NotMutable
public final class MarketField<V extends Value<V>> extends
		ParaEnumBase<V, MarketField<V>> implements BitSetEnum<MarketField<?>> {

	// ##################################

	/** primary key */
	public static final MarketField<Instrument> INSTRUMENT = NEW(Instrument.NULL_INSTRUMENT);

	/** last time any change of any market field */
	public static final MarketField<TimeValue> MARKET_TIME = NEW(NULL_TIME);

	/** last trade, any source */
	public static final MarketField<MarketTrade> TRADE = NEW(NULL_TRADE);

	/** market depth, a.k.a book */
	public static final MarketField<MarketBook> BOOK = NEW(NULL_BOOK);

	/** proxy to last updated entry */
	public static final MarketField<MarketBookEntry> BOOK_LAST = NEW(NULL_BOOK_ENTRY);

	/** proxy to market depth top bid & top ask */
	public static final MarketField<MarketBookTop> BOOK_TOP = NEW(NULL_BOOK_TOP);

	/** cumulative volume - price / size ladder */
	public static final MarketField<MarketCuvol> CUVOL = NEW(NULL_CUVOL);

	/** proxy to last updated entry in cumulative volume */
	public static final MarketField<MarketCuvolEntry> CUVOL_LAST = NEW(NULL_CUVOL_ENTRY);

	// market bars aka sessions

	/** current or default or combo = electronic + manual */
	public static final MarketField<MarketBar> BAR_CURRENT = NEW(NULL_BAR);

	/** extra / stocks form t */
	public static final MarketField<MarketBar> BAR_CURRENT_EXT = NEW(NULL_BAR);

	/** past - previous day; default or combo */
	public static final MarketField<MarketBar> BAR_PREVIOUS = NEW(NULL_BAR);

	// MarketState
	public static final MarketField<MarketState> STATE = NEW(NULL_STATE);

	//

	/** market container with all market fields (self reference) */
	// NOTE: keep market last
	public static final MarketField<Market> MARKET = NEW(NULL_MARKET);

	// ##################################

	public static int size() {
		return values().length;
	}

	public static MarketField<?>[] values() {
		return DictEnum.valuesForType(MarketField.class);
	}

	//

	private final long mask;

	@Override
	public long mask() {
		return mask;
	}

	private MarketField() {
		super();
		mask = 0;
	}

	private MarketField(final V value) {
		super("", value);
		mask = ONE << ordinal();
	}

	private static final <X extends Value<X>> MarketField<X> NEW(final X value) {
		return new MarketField<X>(value);
	}

}
