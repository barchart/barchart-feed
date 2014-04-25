package com.barchart.feed.meta.instrument;

import java.util.Collections;
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

	private volatile InstrumentID id = InstrumentID.NULL;

	@Override
	public SecurityType securityType() {
		return SecurityType.NULL_TYPE;
	}

	@Override
	public BookLiquidityType liquidityType() {
		return BookLiquidityType.NONE;
	}

	@Override
	public BookStructureType bookStructure() {
		return BookStructureType.NONE;
	}

	@Override
	public Size maxBookDepth() {
		return Size.NULL;
	}

	@Override
	public VendorID vendor() {
		return VendorID.NULL;
	}

	@Override
	public Map<VendorID, String> vendorSymbols() {
		return Collections.emptyMap();
	}

	@Override
	public String description() {
		return symbol();
	}

	@Override
	public String CFICode() {
		return "XXXXXX";
	}

	@Override
	public Price tickSize() {
		return Price.NULL;
	}

	@Override
	public Price pointValue() {
		return Price.NULL;
	}

	@Override
	public Price transactionPriceConversionFactor() {

		// TODO This field needs to be added to the inst def proto, but in the name of time
		// im just hacking it in here.  This logic will be moved to the xml decoder
		if(CFICode().startsWith("F") && (symbol().startsWith("SI") || symbol().startsWith("HG"))) {
			return VALUES.newPrice(1, -2);
		}

		if(CFICode().startsWith("F") && symbol().startsWith("J6")) {
			return VALUES.newPrice(1, 2);
		}

		return Price.ONE;
	}

	@Override
	public Calendar calendar() {
		return Calendar.NULL;
	}

	@Override
	public Schedule schedule() {
		return Schedule.NULL;
	}

	@Override
	public InstrumentID id() {
		if(id.isNull()) {
			id = new InstrumentID(marketGUID());
		}
		return id;
	}

	@Override
	public String symbol() {
		return "NULL_SYMBOL";
	}

	@Override
	public String toString() {
		return symbol();
	}

	@Override
	public String currencyCode() {
		return "NULL CURRENCY";
	}

	@Override
	public String instrumentGroup() {
		return null;
	}

	@Override
	public State state() {
		return State.PASSIVE;
	}

	@Override
	public ChannelID channel() {
		return ChannelID.NULL;
	}

	@Override
	public DateTime created() {
		return new DateTime(0);
	}

	@Override
	public DateTime updated() {
		return new DateTime(0);
	}

	@Override
	public DateTimeZone timeZone() {
		return null;
	}

	@Override
	public PriceFormat priceFormat() {
		return PriceFormat.NULL;
	}

	@Override
	public PriceFormat optionStrikePriceFormat() {
		return PriceFormat.NULL;
	}

	@Override
	public List<InstrumentID> components() {
		return Collections.emptyList();
	}

	@Override
	public InstrumentID underlier() {
		return InstrumentID.NULL;
	}

	@Override
	public Price strikePrice() {
		return Price.NULL;
	}

	@Override
	public OptionType optionType() {
		return OptionType.NULL;
	}

	@Override
	public OptionStyle optionStyle() {
		return OptionStyle.DEFAULT;
	}

	@Override
	public SpreadType spreadType() {
		return SpreadType.UNKNOWN;
	}

	@Override
	public List<SpreadLeg> spreadLegs() {
		return Collections.emptyList();
	}

}
