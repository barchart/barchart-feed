/*
 *
 */
package com.ddfplus.feed.api.market.values;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.ddfplus.feed.api.market.enums.MarketBookSide;

@NotMutable
public interface MarketBook extends Value<MarketBook> {

	/**
	 * starting value for place() on bid or ask side; book entry place() can be
	 * TOP ... LIMIT inclusive
	 */
	int ENTRY_TOP = 1;

	/**
	 * maximum number of entries in the bid or ask side;
	 * 
	 * total maximum book height is 64 + price steps in the bid/ask gap;
	 */
	int ENTRY_LIMIT = 32;

	//

	/**
	 * time of the last book update, any side, any place;
	 **/
	TimeValue time();

	/**
	 * non null entries; ordered by price; carry place information;
	 * 
	 * @param side
	 *            the book side
	 * @return array - the market entry[]; not null
	 * @throws NullPointerException
	 *             when side is null
	 * @throws IllegalArgumentException
	 *             when side is not BID nor ASK
	 */
	MarketBookEntry[] entries(MarketBookSide side) throws NullPointerException,
			IllegalArgumentException;

	/* TODO consider to remove */

	/** top price: bid or ask; or NULL_PRICE if invalid */
	PriceValue priceTop(final MarketBookSide side);

	/** top size: bid or ask; or NULL_SIZE if invalid */
	SizeValue sizeTop(final MarketBookSide side);

	/** price-ladder spaced size array */
	SizeValue[] sizes(MarketBookSide side) throws NullPointerException;

	/** top ask - top bid; or NULL_PRICE if there is no gap */
	PriceValue priceGap();

}
