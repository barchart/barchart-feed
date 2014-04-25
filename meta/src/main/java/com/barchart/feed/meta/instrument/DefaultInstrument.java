package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.Event.Type;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

public abstract class DefaultInstrument implements Instrument {

	protected static final ValueFactory vals = ValueFactoryImpl.instance;

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
			return vals.newPrice(1, -2);
		}

		if(CFICode().startsWith("F") && symbol().startsWith("J6")) {
			return vals.newPrice(1, 2);
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

	@Override
	public boolean isNull() {
		return this == Instrument.NULL;
	}

	/*
	 * Alias methods
	 */

	@Override
	public DateTime delivery() {

		final Event event = calendar().event(Type.LAST_DELIVERY_DATE);

		if (event == null || event.isNull()) {
			return null;
		}

		return event.date();

	}

	@Override
	public DateTime expiration() {

		final Event event = calendar().event(Type.LAST_TRADE_DATE);

		if (event == null || event.isNull()) {
			return null;
		}

		return event.date();

	}

	/*
	 * Object comparisons
	 */

	@Override
	public int compareTo(final Instrument o) {
		return id().compareTo(o.id());
	}

	@Override
	public boolean equals(final Object o) {

		if (!(o instanceof Instrument)) {
			return false;
		}

		return compareTo((Instrument) o) == 0;

	}

	@Override
	public int hashCode() {
		return id().hashCode();
	}

	/*
	 * Deprecated stuff.
	 */

	@Override
	public TimeInterval lifetime() {

		final Event start = calendar().event(Event.Type.FIRST_TRADE_DATE);
		final Event end = calendar().event(Event.Type.LAST_TRADE_DATE);

		if (start.isNull() && end.isNull()) {
			return TimeInterval.NULL;
		}

		return vals.newTimeInterval(
				start.isNull() ? 0 : start.date().getMillis(),
				end.isNull() ? 0 : end.date().getMillis());

	}

	@Override
	public Time contractExpire() {

		final Event event = calendar().event(Type.LAST_TRADE_DATE);

		if (event.isNull()) {
			return Time.NULL;
		}

		return vals.newTime(event.date().getMillis());

	}

	@Override
	public Month contractDeliveryMonth() {

		final Event event = calendar().event(Type.LAST_DELIVERY_DATE);

		if (event.isNull()) {
			return Month.NULL_MONTH;
		}

		switch (event.date().getMonthOfYear()) {

			case 1:
				return Month.JANUARY;
			case 2:
				return Month.FEBRUARY;
			case 3:
				return Month.MARCH;
			case 4:
				return Month.APRIL;
			case 5:
				return Month.MAY;
			case 6:
				return Month.JUNE;
			case 7:
				return Month.JULY;
			case 8:
				return Month.AUGUST;
			case 9:
				return Month.SEPTEMBER;
			case 10:
				return Month.OCTOBER;
			case 11:
				return Month.NOVEMEBR;
			case 12:
				return Month.DECEMBER;
			default:
				return Month.NULL_MONTH;

		}

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

		final List<InstrumentID> legs = new ArrayList<InstrumentID>();

		for (final SpreadLeg leg : spreadLegs())
			legs.add(leg.instrument());

		return legs;
	}


	@Override
	public String marketGUID() {
		return id().toString();
	}

	@Override
	public Fraction displayFraction() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Schedule marketHours() {
		return schedule();
	}

	@Override
	public Exchange exchange() {
		return Exchange.NULL;
	}

}
