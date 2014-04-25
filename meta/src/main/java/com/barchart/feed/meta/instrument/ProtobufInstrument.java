package com.barchart.feed.meta.instrument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openfeed.InstrumentDefinition;
import org.openfeed.InstrumentDefinition.Decimal;
import org.openfeed.InstrumentDefinition.EventType;
import org.openfeed.InstrumentDefinition.InstrumentType;
import org.openfeed.InstrumentDefinition.Symbol;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.ChannelID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.Event;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.api.model.meta.instrument.Schedule;
import com.barchart.feed.api.model.meta.instrument.SpreadLeg;
import com.barchart.feed.api.model.meta.instrument.SpreadType;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;

/**
 * Instrument implementation wrapped around a protobuf InstrumentDefinition
 * message.
 */
public class ProtobufInstrument extends InstrumentBase implements Instrument {

	protected static final ValueFactory factory = ValueFactoryImpl.instance;

	protected volatile InstrumentDefinition def = InstrumentDefinition.getDefaultInstance();

	private final SecurityType secType;

	public ProtobufInstrument(final InstrumentDefinition def_) {

		def = def_;

		if (!def.hasInstrumentType() || def.getInstrumentType() == InstrumentType.NO_INSTRUMENT) {

			secType = SecurityType.fromCFI(CFICode());

		} else {

			switch (def.getInstrumentType()) {
				default:
					secType = SecurityType.NULL_TYPE;
					break;
				case FOREX_INSTRUMENT:
					secType = SecurityType.FOREX;
					break;
				case INDEX_INSTRUMENT:
					secType = SecurityType.INDEX;
					break;
				case EQUITY_INSTRUMENT:
					secType = SecurityType.EQUITY;
					break;
				case FUTURE_INSTRUMENT:
					secType = SecurityType.FUTURE;
					break;
				case OPTION_INSTRUMENT:
					secType = SecurityType.OPTION;
					break;
			}

		}

	}

	@Override
	public InstrumentID id() {
		return new InstrumentID(String.valueOf(def.getMarketId()));
	}

	@Override
	public SecurityType securityType() {
		return secType;
	}

	@Override
	public BookLiquidityType liquidityType() {

		if (!def.hasBookLiquidity()) {
			return BookLiquidityType.NONE;
		}

		switch (def.getBookLiquidity()) {
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

		if (!def.hasBookStructure()) {
			return BookStructureType.NONE;
		}

		switch (def.getBookStructure()) {
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

		if (!def.hasBookDepth()) {
			return Size.NULL;
		}

		return factory.newSize(def.getBookDepth(), 0);

	}

	@Override
	public VendorID vendor() {

		if (!def.hasVendorId()) {
			return VendorID.NULL;
		}

		return new VendorID(def.getVendorId());

	}

	@Override
	public Map<VendorID, String> vendorSymbols() {

		final Map<VendorID, String> map = new HashMap<VendorID, String>();

		for (final Symbol symbol : def.getSymbolsList()) {
			map.put(new VendorID(symbol.getVendor()), symbol.getSymbol());
		}

		return Collections.unmodifiableMap(map);

	}

	@Override
	public String symbol() {

		if (!def.hasSymbol()) {
			return "Null Symbol";
		}

		return def.getSymbol();

	}

	@Override
	public String description() {

		if (!def.hasDescription()) {
			return "Null Description";
		}

		return def.getDescription();

	}

	@Override
	public String CFICode() {

		if (!def.hasCfiCode()) {
			return "Null CFICode";
		}

		return def.getCfiCode();

	}

	@Override
	public String exchangeCode() {

		if (!def.hasExchangeCode()) {
			return "Null Exchange Code";
		}

		return def.getExchangeCode();

	}

	@Override
	public String currencyCode() {

		if (!def.hasCurrencyCode())
			return null;

		return def.getCurrencyCode();

	}

	@Override
	public String instrumentGroup() {

		if (!def.hasInstrumentGroup())
			return null;

		return def.getInstrumentGroup();

	}

	@Override
	public State state() {

		if (def.hasState()) {

			switch (def.getState()) {

				case ACTIVE_STATE:
					return State.ACTIVE;

				case PASSIVE_STATE:
				default:

			}

		}

		return State.PASSIVE;

	}

	@Override
	public ChannelID channel() {

		if (!def.hasChannel())
			return ChannelID.NULL;

		return new ChannelID(def.getChannel());

	}

	@Override
	public DateTime created() {

		if (!def.hasRecordCreateTime())
			return null;

		return new DateTime(def.getRecordCreateTime());

	}

	@Override
	public DateTime updated() {

		if (!def.hasRecordUpdateTime())
			return null;

		return new DateTime(def.getRecordUpdateTime());

	}

	@Override
	public Calendar calendar() {

		if (!def.hasCalendar())
			return Calendar.NULL;

		final DefaultCalendar calendar = new DefaultCalendar();

		for (final InstrumentDefinition.Event evt : def.getCalendar().getEventsList()) {
			calendar.add(new DefaultEvent(type(evt.getType()), new DateTime(evt.getDate())));
		}

		return calendar;

	}

	private Event.Type type(final EventType type) {

		switch (type) {

			case FIRST_DELIVERY_DATE:
				return Event.Type.FIRST_DELIVERY_DATE;

			case FIRST_HOLDING_DATE:
				return Event.Type.FIRST_HOLDING_DATE;

			case FIRST_NOTICE_DATE:
				return Event.Type.FIRST_NOTICE_DATE;

			case FIRST_POSITION_DATE:
				return Event.Type.FIRST_POSITION_DATE;

			case FIRST_TRADE_DATE:
				return Event.Type.FIRST_TRADE_DATE;

			case LAST_DELIVERY_DATE:
				return Event.Type.LAST_DELIVERY_DATE;

			case LAST_HOLDING_DATE:
				return Event.Type.LAST_HOLDING_DATE;

			case LAST_NOTICE_DATE:
				return Event.Type.LAST_NOTICE_DATE;

			case LAST_POSITION_DATE:
				return Event.Type.LAST_POSITION_DATE;

			case LAST_TRADE_DATE:
				return Event.Type.LAST_TRADE_DATE;

			case SETTLEMENT_DATE:
				return Event.Type.SETTLEMENT_DATE;

			default:
				return Event.Type.UNKNOWN;

		}

	}

	@Override
	public Schedule schedule() {

		if (!def.hasSchedule() || def.getSchedule().getSessionsCount() == 0) {
			return Schedule.NULL;
		}

		final InstrumentDefinition.Schedule s = def.getSchedule();
		final DefaultSchedule schedule = new DefaultSchedule();

		for (int i = 0; i < s.getSessionsCount(); i++) {
			final InstrumentDefinition.TimeSpan session = s.getSessions(i);
			schedule.add(new DefaultTimeSpan(session.getTimeStart(), session.getTimeFinish()));
		}

		return schedule;

	}

	@Override
	public Price tickSize() {

		if (!def.hasMinimumPriceIncrement()) {
			return Price.NULL;
		}

		final Decimal d = def.getMinimumPriceIncrement();
		return factory.newPrice(d.getMantissa(), d.getExponent());

	}

	@Override
	public Price pointValue() {

		if (!def.hasContractPointValue()) {
			return Price.NULL;
		}

		final Decimal d = def.getContractPointValue();
		return factory.newPrice(d.getMantissa(), d.getExponent());

	}

	@Override
	public Price transactionPriceConversionFactor() {

		if (!def.hasTransactionPriceConversionFactor()) {
			return vals.newPrice(1);
		}

		return vals.newPrice(def.getTransactionPriceConversionFactor());

	}

	@Override
	public DateTimeZone timeZone() {
		return DateTimeZone.forID(def.getTimeZoneName());
	}

	@Override
	public PriceFormat priceFormat() {

		if (!def.hasPriceFormat())
			return PriceFormat.NULL;

		final InstrumentDefinition.PriceFormat format = def.getPriceFormat();

		return new DefaultPriceFormat(
				format.getDenominator(),
				format.getPrecision(),
				format.getIsFractional(),
				format.getSubDenominator(),
				format.getSubPrecision(),
				subFormat(format.getSubFormat()));

	}

	@Override
	public PriceFormat optionStrikePriceFormat() {

		if (!def.hasOptionStrikePriceFormat())
			return PriceFormat.NULL;

		final InstrumentDefinition.PriceFormat format = def.getOptionStrikePriceFormat();

		return new DefaultPriceFormat(
				format.getDenominator(),
				format.getPrecision(),
				format.getIsFractional(),
				format.getSubDenominator(),
				format.getSubPrecision(),
				subFormat(format.getSubFormat()));

	}

	private PriceFormat.SubFormat subFormat(final InstrumentDefinition.PriceFormat.SubFormat format) {

		switch (format) {

			case DECIMAL:
				return PriceFormat.SubFormat.DECIMAL;

			case FRACTIONAL:
				return PriceFormat.SubFormat.FRACTIONAL;

			case FLAT:
			default:
				return PriceFormat.SubFormat.FLAT;

		}

	}

	@Override
	public List<InstrumentID> components() {

		final List<InstrumentID> components = new ArrayList<InstrumentID>();

		for (final long id : def.getComponentIdList()) {
			components.add(new InstrumentID(String.valueOf(id)));
		}

		return components;

	}

	@Override
	public InstrumentID underlier() {

		if (!def.hasUnderlyingMarketId())
			return InstrumentID.NULL;

		return new InstrumentID(String.valueOf(def.getUnderlyingMarketId()));

	}

	@Override
	public Price strikePrice() {

		if (!def.hasOptionStrike())
			return Price.NULL;

		final Decimal d = def.getOptionStrike();
		return factory.newPrice(d.getMantissa(), d.getExponent());

	}

	@Override
	public OptionType optionType() {

		if (!def.hasOptionType())
			return OptionType.NULL;

		switch (def.getOptionType()) {

			case CALL_OPTION:
				return OptionType.CALL;

			case PUT_OPTION:
				return OptionType.PUT;

			default:
				return OptionType.NULL;

		}

	}

	@Override
	public OptionStyle optionStyle() {

		if (!def.hasOptionStyle())
			return OptionStyle.NULL;

		switch (def.getOptionStyle()) {

			case AMERICAN_STYLE:
				return OptionStyle.AMERICAN;

			case EUROPEAN_STYLE:
				return OptionStyle.EUROPEAN;

			case DEFAULT_STYLE:
				return OptionStyle.DEFAULT;

			default:
				return OptionStyle.NULL;

		}

	}

	@Override
	public SpreadType spreadType() {

		// TODO Need to sync up spread enums
		return SpreadType.NULL;

	}

	@Override
	public List<SpreadLeg> spreadLegs() {

		final List<SpreadLeg> legs = new ArrayList<SpreadLeg>();

		for (final InstrumentDefinition.SpreadLeg leg : def.getSpreadLegList()) {
			legs.add(new DefaultSpreadLeg(new InstrumentID(String.valueOf(leg.getMarketId())), leg.getRatio()));
		}

		return legs;

	}

	@Override
	public String toString() {
		return def.toString();
	}

	public InstrumentDefinition definition() {
		return def;
	}

}
