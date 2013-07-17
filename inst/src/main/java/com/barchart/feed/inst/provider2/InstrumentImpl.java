package com.barchart.feed.inst.provider2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openfeed.proto.inst.Calendar;
import org.openfeed.proto.inst.Decimal;
import org.openfeed.proto.inst.InstrumentDefinition;
import org.openfeed.proto.inst.Interval;

import com.barchart.feed.api.filter.Filterable.MetaType;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Instrument.BookLiquidityType;
import com.barchart.feed.api.model.meta.Instrument.BookStructureType;
import com.barchart.feed.api.model.meta.Instrument.SecurityType;
import com.barchart.feed.api.util.Identifier;
import com.barchart.feed.inst.participant.InstrumentState.State;
import com.barchart.feed.inst.provider.Exchanges;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;

class InstrumentImpl implements Instrument {
	
	private static final Factory factory = FactoryLoader.load();
	
	protected volatile InstrumentDefinition def = 
			InstrumentDefinition.getDefaultInstance();
	protected volatile State state = State.PARTIAL;
	
	public InstrumentImpl(final InstrumentDefinition def) {
		this.def = def;
	}

	@Override
	public String marketGUID() {
		
		if(!def.hasMarketId()) {
			return "NULL";
		}
		
		/* Symbol is currently GUID */
		return def.getSymbol();
	}

	@Override
	public SecurityType securityType() {
		
		if(!def.hasInstrumentType()) {
			return SecurityType.NULL_TYPE;
		}
		
		switch(def.getInstrumentType()) {
		default:
			return SecurityType.NULL_TYPE;
		case FOREX_INSTRUMENT:
			return SecurityType.FOREX;
		case INDEX_INSTRUMENT:
			return SecurityType.INDEX;
		case EQUITY_INSTRUMENT:
			return SecurityType.EQUITY;
		case FUTURE_INSTRUMENT:
			return SecurityType.FUTURE;
		case OPTION_INSTRUMENT:
			return SecurityType.OPTION;
		case SPREAD_INSTRUMENT:
			return SecurityType.SPREAD;
		}
	}

	@Override
	public BookLiquidityType liquidityType() {
		
		if(!def.hasBookLiquidity()) {
			return BookLiquidityType.NONE;
		}
		
		switch(def.getBookLiquidity()) {
		default:
			return BookLiquidityType.NONE;
		case DEFAULT_LIQUIDITY:
			return BookLiquidityType.DEFAULT;
		case IMPLIED_LIQUIDITY:
			return BookLiquidityType.IMPLIED;
		case COMBINED_LIQUIDITY:
			return BookLiquidityType.COMBINED;
		}
	}

	@Override
	public BookStructureType bookStructure() {
		
		if(!def.hasBookStructure()) {
			return BookStructureType.NONE;
		}
		
		switch(def.getBookStructure()) {
		default:
			return BookStructureType.NONE;
		case LEVEL_STRUCTURE:
			return BookStructureType.PRICE_LEVEL;
		case PRICE_STRUCTURE:
			return BookStructureType.PRICE_VALUE;
		case ORDER_STRUCTURE:
			return BookStructureType.ORDER_NUMBER;
		}
	}

	@Override
	public Size maxBookDepth() {
		
		if(!def.hasBookDepth()) {
			return Size.NULL;
		}
		
		return factory.newSize(def.getBookDepth(), 0);
	}

	@Override
	public String instrumentDataVendor() {
		
		if(!def.hasVendorId()) {
			return "Null Vendor";
		}
		
		return def.getVendorId();
	}

	@Override
	public String symbol() {
		
		if(!def.hasSymbol()) {
			return "Null Symbol";
		}
		
		return def.getSymbol();
	}

	@Override
	public String description() {
		
		if(!def.hasDescription()) {
			return "Null Description";
		}
		
		return def.getDescription();
	}

	@Override
	public String CFICode() {
		
		if(!def.hasCfiCode()) {
			return "Null CFICode";
		}
		
		return def.getCfiCode();
	}

	@Override
	public Exchange exchange() {
		
		if(!def.hasExchangeCode()) {
			return Exchange.NULL;
		}
		
		return Exchanges.fromCode(def.getExchangeCode());
	}

	@Override
	public String exchangeCode() {
		
		if(!def.hasExchangeCode()) {
			return "Null Exchange Code";
		}
		
		return def.getExchangeCode();
	}

	@Override
	public Price tickSize() {
		
		if(!def.hasMinimumPriceIncrement()) {
			return Price.NULL;
		}
		
		final Decimal d = def.getMinimumPriceIncrement();
		return factory.newPrice(d.getMantissa(), d.getExponent());
	}

	@Override
	public Price pointValue() {
		
		if(!def.hasContractPointValue()) {
			return Price.NULL;
		}
		
		final Decimal d = def.getContractPointValue();
		return factory.newPrice(d.getMantissa(), d.getExponent());
	}

	@Override
	public Fraction displayFraction() {
		
		if(!def.hasDisplayBase()) {
			return Fraction.NULL;
		}
		
		// TODO revisit fraction, num / denom doesn't fit with base / exponent
		return factory.newFraction(1, 
				(int)Math.pow(def.getDisplayBase(), def.getDisplayExponent()));
	}

	@Override
	public TimeInterval lifetime() {
		
		if(!def.hasSymbolExpiration()) {
			return TimeInterval.NULL;
		}
		
		// TODO
		//return factory.newTimeInterval(0, def.getSymbolExpiration().);
		return null;
	}

	@Override
	public Schedule marketHours() {
		
		if(!def.hasCalendar()) {
			return Schedule.NULL;
		}

		final Calendar c = def.getCalendar();
		final TimeInterval[] ti = new TimeInterval[c.getMarketHoursCount()];
		for(int i = 0; i < c.getMarketHoursCount(); i++) {
			final Interval inter = c.getMarketHours(i);
			ti[i] = factory.newTimeInterval(inter.getTimeStart(), 
					inter.getTimeFinish());
		}
		
		factory.newSchedule(ti);
		
		return null;
	}

	@Override
	public long timeZoneOffset() {
		
		// This method needs to be removed
		
		return 0;
	}

	@Override
	public String timeZoneName() {
		
		if(!def.hasTimeZoneName()) {
			return "Null Time Zone";
		}
		
		return def.getTimeZoneName();
	}

	@Override
	public List<Identifier> componentLegs() {
		
		if(def.getComponentIdList().size() == 0) {
			return Collections.emptyList();
		}
		
		final List<Identifier> legs = new ArrayList<Identifier>();
		for(final Long l : def.getComponentIdList()) {
			legs.add(new IdentifierImpl(String.valueOf(l)));
		}
		
		return legs;
	}

	@Override
	public int compareTo(Instrument o) {
		return id().compareTo(o.id());
	}

	@Override
	public boolean isNull() {
		return state == State.NULL;
	}

	@Override
	public Identifier id() {
		return new IdentifierImpl(marketGUID());
	}

	@Override
	public boolean equals(final Object o) {
		
		if(!(o instanceof Instrument)) {
			return false;
		}
		
		System.out.println("Equals in inst state fac impl " + id().toString() + " " +
				((Instrument)o).id().toString());
		
		return compareTo((Instrument)o) == 0;
		
	}
	
	@Override
	public MetaType type() {
		return MetaType.INSTRUMENT;
	}

}
