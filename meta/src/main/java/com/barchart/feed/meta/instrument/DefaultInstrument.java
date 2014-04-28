package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/**
 * Baseline instrument with mostly null values for extending.
 */
public abstract class DefaultInstrument extends InstrumentBase {

	protected final Map<VendorID, String> vendorSymbols = new HashMap<VendorID, String>();
	protected final List<InstrumentID> components = new ArrayList<InstrumentID>();
	protected final List<SpreadLeg> spreadLegs = new ArrayList<SpreadLeg>();

	protected final InstrumentID id;

	protected SecurityType securityType = SecurityType.NULL_TYPE;
	protected BookLiquidityType liquidityType = BookLiquidityType.NONE;
	protected BookStructureType bookStructure = BookStructureType.NONE;
	protected Size maxBookDepth = Size.NULL;
	protected VendorID vendor = VendorID.NULL;
	protected String description = symbol();
	protected String CFICode = "XXXXXX";
	protected Price tickSize = Price.NULL;
	protected Price pointValue = Price.NULL;
	protected Calendar calendar = Calendar.NULL;
	protected Schedule schedule = Schedule.NULL;
	protected String symbol = "NULL_SYMBOL";
	protected String toString = symbol();
	protected String currencyCode = "NULL CURRENCY";
	protected String exchangeCode = "NULL EXCHANGE";
	protected String instrumentGroup = null;
	protected State state = State.PASSIVE;
	protected ChannelID channel = ChannelID.NULL;
	protected DateTime created = new DateTime(0);
	protected DateTime updated = new DateTime(0);
	protected DateTimeZone timeZone = null;
	protected PriceFormat priceFormat = PriceFormat.NULL;
	protected PriceFormat optionStrikePriceFormat = PriceFormat.NULL;
	protected Price transactionPriceConversionFactor = Price.ONE;
	protected InstrumentID underlier = InstrumentID.NULL;
	protected Price strikePrice = Price.NULL;
	protected OptionType optionType = OptionType.NULL;
	protected OptionStyle optionStyle = OptionStyle.DEFAULT;
	protected SpreadType spreadType = SpreadType.UNKNOWN;

	protected DefaultInstrument(final InstrumentID id_) {
		id = id_;
	}

	@Override
	public InstrumentID id() {
		return id;
	}

	@Override
	public String symbol() {
		return symbol;
	}

	@Override
	public SecurityType securityType() {
		return securityType;
	}

	@Override
	public BookLiquidityType liquidityType() {
		return liquidityType;
	}

	@Override
	public BookStructureType bookStructure() {
		return bookStructure;
	}

	@Override
	public Size maxBookDepth() {
		return maxBookDepth;
	}

	@Override
	public VendorID vendor() {
		return vendor;
	}

	@Override
	public Map<VendorID, String> vendorSymbols() {
		return vendorSymbols;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String currencyCode() {
		return currencyCode;
	}

	@Override
	public String exchangeCode() {
		return exchangeCode;
	}

	@Override
	public String CFICode() {
		return CFICode;
	}

	@Override
	public Price tickSize() {
		return tickSize;
	}

	@Override
	public Price pointValue() {
		return pointValue;
	}

	@Override
	public Price transactionPriceConversionFactor() {
		return transactionPriceConversionFactor;
	}

	@Override
	public Calendar calendar() {
		return calendar;
	}

	@Override
	public Schedule schedule() {
		return schedule;
	}

	@Override
	public String instrumentGroup() {
		return instrumentGroup;
	}

	@Override
	public State state() {
		return state;
	}

	@Override
	public ChannelID channel() {
		return channel;
	}

	@Override
	public DateTime created() {
		return created;
	}

	@Override
	public DateTime updated() {
		return updated;
	}

	@Override
	public DateTimeZone timeZone() {
		return timeZone;
	}

	@Override
	public PriceFormat priceFormat() {
		return priceFormat;
	}

	@Override
	public PriceFormat optionStrikePriceFormat() {
		return optionStrikePriceFormat;
	}

	@Override
	public List<InstrumentID> components() {
		return components;
	}

	@Override
	public InstrumentID underlier() {
		return underlier;
	}

	@Override
	public Price strikePrice() {
		return strikePrice;
	}

	@Override
	public OptionType optionType() {
		return optionType;
	}

	@Override
	public OptionStyle optionStyle() {
		return optionStyle;
	}

	@Override
	public SpreadType spreadType() {
		return spreadType;
	}

	@Override
	public List<SpreadLeg> spreadLegs() {
		return spreadLegs;
	}

	@Override
	public String toString() {
		return exchangeCode() + ":" + symbol();
	}

}
