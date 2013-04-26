package com.barchart.feed.book.provider;

import org.joda.time.DateTime;

import com.barchart.feed.api.book.OrderBookAction;
import com.barchart.feed.api.data.MarketTag;
import com.barchart.feed.api.data.framework.PriceLevel;
import com.barchart.feed.api.enums.BookLiquidityType;
import com.barchart.feed.api.enums.MarketSide;
import com.barchart.feed.api.message.Snapshot;
import com.barchart.feed.api.message.Update;
import com.barchart.missive.core.ObjectMapSafe;
import com.barchart.util.math.MathExtra;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;

public class PriceLevelBase extends ObjectMapSafe implements PriceLevel {

	private final static byte nulAct = OrderBookAction.NOOP.ord;
	private final static byte nulSide = MarketSide.GAP.ord;
	private final static byte nulType = BookLiquidityType.NONE.ord;
	
	// store byte ordinal to save heap
	private final byte ordAct;
	private final byte ordSide;
	private final byte ordType;

	// place expected to fit byte size
	private final byte place;
	
	private final PriceValue price;
	private final SizeValue quantity;
	
	public PriceLevelBase(final OrderBookAction act, final MarketSide side, 
			final BookLiquidityType type, final int place, final PriceValue price,
			final SizeValue quantity) throws ArithmeticException {
		
		this.ordAct = (act == null ? nulAct : act.ord);
		this.ordSide = (side == null ? nulSide : side.ord);
		this.ordType = (type == null ? nulType : type.ord);

		this.place = MathExtra.castIntToByte(place);

		this.price = price;
		this.quantity = quantity;
		
	}
	
	@Override
	public double priceLevel() {
		return price.asDouble();
	}

	@Override
	public long quantityAtPrice() {
		return quantity.asLong();
	}

	@Override
	public PriceValue price() {
		return price;
	}

	@Override
	public SizeValue quantity() {
		return quantity;
	}
	
	@Override
	public OrderBookAction act() {
		return OrderBookAction.fromOrd(ordAct);
	}
	
	@Override
	public MarketSide side() {
		return MarketSide.fromOrd(ordSide);
	}

	@Override
	public int place() {
		return place;
	}

	@Override
	public BookLiquidityType type() {
		return BookLiquidityType.fromOrd(ordType);
	}

	@Override
	public DateTime lastTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Update<PriceLevel> lastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Snapshot<PriceLevel> lastSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketTag<PriceLevel> tag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Update<PriceLevel> update) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void snapshot(Snapshot<PriceLevel> snapshot) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return String.format("%s   %s   %s   %s", side(), place(), priceLevel(),
				size());
	}
	
//	@Override
//	public final int hashCode() {
//		return ((ordAct << 24) | (ordSide << 16) | (ordType << 8))
//				^ (price().hashCode()) ^ (size().hashCode());
//	}

	public String toStringFull() {
		return String.format("%s   %s   %s   %s   %s   %s", act(), side(),
				type(), place(), priceLevel(), size());
	}

	private static final void checkNull(final Object value) {
		if (value == null) {
			throw new NullPointerException("invalid implementation");
		}
	}

	

}
