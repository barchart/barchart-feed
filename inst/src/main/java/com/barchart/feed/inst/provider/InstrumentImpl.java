package com.barchart.feed.inst.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.Calendar;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Interval;
import org.openfeed.InstrumentDefinition.Symbol;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.inst.participant.InstrumentState.State;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public class InstrumentImpl extends InstrumentBase implements Instrument {
	
	private static final ValueFactory factory = new ValueFactoryImpl();
	
	protected volatile InstrumentDefinition def = 
			InstrumentDefinition.getDefaultInstance();
	protected volatile State state = State.PARTIAL;
	
	public InstrumentImpl(final InstrumentDefinition def) {
		this.def = def;
	}

	@Override
	public String marketGUID() {
		
		/* Symbol is currently GUID */
		if(!def.hasSymbol()) {
			return "NULL";
		}
		
		/* Symbol is currently GUID */
		return def.getSymbol();
	}
	
	@Override
	public SecurityType securityType() {
		
		if(!def.hasInstrumentType() || 
				def.getInstrumentType() == InstrumentType.NO_INSTRUMENT) {
			return SecurityType.fromCFI(CFICode());
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
	public Map<VendorID, String> vendorSymbols() {
		
		final Map<VendorID, String> map = new HashMap<VendorID, String>();
		
		for(final Symbol symbol : def.getSymbolsList()) {
			map.put(new VendorID(symbol.getVendor()), symbol.getSymbol());
		}
		
		return Collections.unmodifiableMap(map);
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
		
		return factory.newFraction(def.getDisplayBase(), def.getDisplayExponent());
	}

	@Override
	public TimeInterval lifetime() {
//		
//		if(!def.hasCalendar()) {
//			return TimeInterval.NULL;
//		}
//		
//		if(!def.getCalendar().hasLifeTime()) {
//			return TimeInterval.NULL;
//		}
//		
//		final Interval i = def.getCalendar().getLifeTime();
//		
//		if(i.getTimeFinish() == 0) {
//			return TimeInterval.NULL;
//		}
//		
//		return factory.newTimeInterval(i.getTimeStart(), i.getTimeFinish());
		return TimeInterval.NULL;
	}

	@Override
	public Schedule marketHours() {
		
		if(!def.hasCalendar()) {
			return Schedule.NULL;
		}

		if(def.getCalendar().getMarketHoursCount() == 0) {
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
	public Time contractExpire() {
		return vals.newTime(def.getContractExpire());
	}
	
	@Override
	public Month contractDeliveryMonth() {
		
		switch(def.getContractMonth()) {
			case JANUARY:
				return Month.JANUARY;
			case FEBRUARY:
				return Month.FEBRUARY;
			case MARCH:
				return Month.MARCH;
			case APRIL:
				return Month.APRIL;
			case MAY:
				return Month.MAY;
			case JUNE:
				return Month.JUNE;
			case JULY:
				return Month.JULY;
			case AUGUST:
				return Month.AUGUST;
			case SEPTEMBER:
				return Month.SEPTEMBER;
			case OCTOBER:
				return Month.OCTOBER;
			case NOVEMBER:
				return Month.NOVEMEBR;
			case DECEMBER:
				return Month.DECEMBER;
		}
		
		return Month.NULL_MONTH;
		
	}

	@Override
	public long timeZoneOffset() {
		
		if(!def.hasTimeZoneName()) {
			return 0l;
		}
		
		/* Hack because extras "time zone name" isn't actually the code for the
		 * timezone */
		final String tzn = timeZoneName();
		
		TimeZone zone;
		if(tzn.equals("NEW_YORK")) {
			zone = TimeZone.getTimeZone("EST");
		} else if(tzn.equals("CHICAGO")) {
			zone = TimeZone.getTimeZone("CST");
		} else {
			zone = TimeZone.getTimeZone(timeZoneName());
		}
		
		return zone.getOffset(System.currentTimeMillis());
	}

	@Override
	public String timeZoneName() {
		
		if(!def.hasTimeZoneName()) {
			return "Null Time Zone";
		}
		
		return def.getTimeZoneName();
	}

	@Override
	public List<InstrumentID> componentLegs() {
		
		if(def.getComponentIdList().size() == 0) {
			return Collections.emptyList();
		}
		
		final List<InstrumentID> legs = new ArrayList<InstrumentID>();
//		for(final Long l : def.getComponentIdList()) {
//			//legs.add(new InstrumentID(l));
//		}
		
		return legs;
	}
	
	@Override
	public String toString() {
		return def.toString();
	}

}
