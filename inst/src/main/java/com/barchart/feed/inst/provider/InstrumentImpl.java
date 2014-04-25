package com.barchart.feed.inst.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.Interval;
import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.Calendar;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Symbol;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public class InstrumentImpl extends InstrumentBase implements Instrument {

	protected static final ValueFactory factory = ValueFactoryImpl.instance;

	protected volatile InstrumentDefinition def = InstrumentDefinition.getDefaultInstance();
	protected volatile LoadState loadState = LoadState.PARTIAL;

	private final SecurityType secType;

	public InstrumentImpl(final InstrumentDefinition def) {
		this.def = def;

		if(!def.hasInstrumentType() || def.getInstrumentType() == InstrumentType.NO_INSTRUMENT) {
			secType = SecurityType.fromCFI(CFICode());
		} else {

			switch(def.getInstrumentType()) {
			default:
				secType = SecurityType.NULL_TYPE;
				break;
			case FOREX_INSTRUMENT:
				secType = SecurityType.FOREX;
				break;
			case INDEX_INSTRUMENT:
				secType =  SecurityType.INDEX;
				break;
			case EQUITY_INSTRUMENT:
				secType =  SecurityType.EQUITY;
				break;
			case FUTURE_INSTRUMENT:
				secType =  SecurityType.FUTURE;
				break;
			case OPTION_INSTRUMENT:
				secType = SecurityType.OPTION;
				break;
			}
		}

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
		return secType;
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
	public VendorID vendor() {

		if(!def.hasVendorId()) {
			return VendorID.NULL;
		}

		return new VendorID(def.getVendorId());
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

	protected volatile Fraction dispFrac = Fraction.NULL;

	@Override
	public Fraction displayFraction() {

		if(!def.hasDisplayBase()) {
			return Fraction.NULL;
		}

		if(dispFrac.isNull()) {
			dispFrac = factory.newFraction(def.getDisplayBase(), def.getDisplayExponent());
		}

		return dispFrac;
	}

	@Override
	public TimeInterval lifetime() {
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
			default:
				return Month.NULL_MONTH;
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
