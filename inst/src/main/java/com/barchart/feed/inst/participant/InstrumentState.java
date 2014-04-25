package com.barchart.feed.inst.participant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public interface InstrumentState extends Instrument, Resettable, Instrumentable {

	enum LoadState {
		/** An invalid instrument definition */
		NULL,
		/** An empty instrument definition with an ID */
		EMPTY,
		/** A partially complete instrument definition */
		PARTIAL,
		/** A complete instrument definition */
		FULL
	}

	LoadState loadState();

	@Override
	void process(Instrument value);

	@Override
	void reset();

	InstrumentState NULL = new InstrumentState() {

		@Override
		public String marketGUID() {
			return "NULL_INSTRUMENT_STATE";
		}

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
		public String symbol() {
			return "NULL_SYMBOL";
		}

		@Override
		public Map<VendorID, String> vendorSymbols() {
			return new HashMap<VendorID, String>(0);
		}

		@Override
		public String description() {
			return "NULL_DESCRIPTION";
		}

		@Override
		public String CFICode() {
			return "NULL_CFI";
		}

		@Override
		public Exchange exchange() {
			return Exchange.NULL;
		}

		@Override
		public String exchangeCode() {
			return "NULL_EXCHANGE_CODE";
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
			return Price.NULL;
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
			return Schedule.NULL;
		}

		@Override
		public Month contractDeliveryMonth() {
			return Month.NULL_MONTH;
		}

		@Override
		public long timeZoneOffset() {
			return 0;
		}

		@Override
		public String timeZoneName() {
			return "NULL_TIMEZONE";
		}

		@Override
		public List<InstrumentID> componentLegs() {
			return Collections.emptyList();
		}

		@Override
		public int compareTo(final Instrument o) {
			return o.compareTo(Instrument.NULL);
		}

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public InstrumentID id() {
			return InstrumentID.NULL;
		}

		@Override
		public MetaType type() {
			return MetaType.INSTRUMENT;
		}

		@Override
		public State state() {
			return State.PASSIVE;
		}

		@Override
		public void process(final Instrument value) {
		}

		@Override
		public void reset() {
		}

		@Override
		public Time contractExpire() {
			return Time.NULL;
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
			return new DateTime(0);
		}

		@Override
		public DateTime expiration() {
			return new DateTime(0);
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
		public LoadState loadState() {
			return LoadState.NULL;
		}

	};

}
