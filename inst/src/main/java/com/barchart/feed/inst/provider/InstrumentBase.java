package com.barchart.feed.inst.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.Channel;
import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public abstract class InstrumentBase implements Instrument {

	protected static final ValueFactory vals = ValueFactoryImpl.instance;

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
		return new HashMap<VendorID, String>(0);
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
	public Exchange exchange() {
		return Exchange.NULL;
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
			return vals.newPrice(1, -2);
		}

		if(CFICode().startsWith("F") && symbol().startsWith("J6")) {
			return vals.newPrice(1, 2);
		}

		return Price.ONE;
	}

	@Override
	public Fraction displayFraction() {
		return Fraction.NULL;
	}

	@Override
	public TimeInterval lifetime() {
		return TimeInterval.NULL;
	}

	@Override
	public Schedule marketHours() {
		return schedule();
	}

	@Override
	public Time contractExpire() {
		return Time.NULL;
	}

	@Override
	public Month contractDeliveryMonth() {
		return Month.NULL_MONTH;
	}

	@Override
	public String timeZoneName() {
		return timeZone().getID();
	}

	@Override
	public long timeZoneOffset() {
		return timeZone().getOffset(System.currentTimeMillis());
	}

	@Override
	public List<InstrumentID> componentLegs() {
		return Collections.<InstrumentID> emptyList();
	}

	protected volatile InstrumentID id = InstrumentID.NULL;

	@Override
	public InstrumentID id() {
		if(id.isNull()) {
			id = new InstrumentID(marketGUID());
		}
		return id;
	}

	@Override
	public int compareTo(final Instrument o) {
		return id().compareTo(o.id());
	}

	@Override
	public boolean equals(final Object o) {

		if(!(o instanceof Instrument)) {
			return false;
		}

		return compareTo((Instrument)o) == 0;

	}

	@Override
	public int hashCode() {
		return id().hashCode();
	}

	@Override
	public boolean isNull() {
		return this == Instrument.NULL;
	}

	@Override
	public MetaType type() {
		return MetaType.INSTRUMENT;
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
		return "NULL GROUP";
	}

	@Override
	public State state() {
		return State.PASSIVE;
	}

	@Override
	public Channel channel() {
		return null;
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
	public Calendar calendar() {
		return Calendar.NULL;
	}

	@Override
	public Schedule schedule() {
		return Schedule.NULL;
	}

	@Override
	public DateTimeZone timeZone() {
		return DateTimeZone.getDefault();
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
	public DateTime delivery() {
		return calendar().event(Event.Type.LAST_DELIVERY_DATE).date();
	}

	@Override
	public DateTime expiration() {
		return calendar().event(Event.Type.LAST_TRADE_DATE).date();
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
		return OptionType.UNKNOWN;
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
