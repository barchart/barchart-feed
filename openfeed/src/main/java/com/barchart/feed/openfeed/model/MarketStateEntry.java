package com.barchart.feed.openfeed.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.openfeed.MarketEntry;
import org.openfeed.MarketEntry.Action;
import org.openfeed.MarketEntry.Builder;
import org.openfeed.MarketEntry.Descriptor;
import org.openfeed.MarketEntry.Type;
import org.openfeed.MarketEntryOrBuilder;
import org.openfeed.util.datetime.DateOnlyValue;
import org.openfeed.util.datetime.DateTimeValue;
import org.openfeed.util.datetime.ProtoDateUtil;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;

public class MarketStateEntry {

	private static final ValueFactory VALUES = ValueFactoryImpl.getInstance();

	final MarketEntryOrBuilder message;

	private DateTime timestamp;
	private LocalDate tradeDate;
	private Price price = null;
	private Size size = null;

	/**
	 * Construct a new wrapper with a mutable message builder.
	 */
	public MarketStateEntry() {
		this(MarketEntry.newBuilder());
	}

	/**
	 * Construct a new wrapper with the given message or builder.
	 */
	public MarketStateEntry(final MarketEntryOrBuilder message_) {
		message = message_;
	}

	/**
	 * Build an immutable protobuf message from the current message or builder. If the underlying message was already
	 * built, it will be returned instead of copied.
	 */
	public MarketEntry message() {

		if (message instanceof MarketEntry) {
			return (MarketEntry) message;
		}

		return builder().build();

	}

	/**
	 * Get the underlying message as a builder, if possible. Throws IllegalStateException if the underlying message is
	 * already built (and thus read-only).
	 */
	private Builder builder() {

		if (!(message instanceof Builder)) {
			throw new IllegalStateException("Message is not a builder");
		}

		return (Builder) message;

	}

	/**
	 * The action to take.
	 */
	public Action action() {
		return message.getAction();
	}

	public MarketStateEntry action(final Action action) {
		builder().setAction(action);
		return this;
	}

	/**
	 * The entry/field type.
	 */
	public Type type() {
		return message.getType();
	}

	public MarketStateEntry type(final Type type) {
		builder().setType(type);
		return this;
	}

	/**
	 * Secondary entry type categories.
	 */
	public List<Descriptor> descriptors() {
		return message.getDescriptorList();
	}

	public MarketStateEntry addDescriptor(final Descriptor descriptor) {
		builder().addDescriptor(descriptor);
		return this;
	}

	/**
	 * The market this entry applies to.
	 */
	public long marketId() {
		return message.getMarketId();
	}

	public MarketStateEntry marketId(final long marketId) {
		builder().setMarketId(marketId);
		return this;
	}

	/**
	 * The feed sequence number for this message.
	 */
	public long sequence() {
		return message.getSequence();
	}

	public MarketStateEntry sequence(final long sequence) {
		builder().setSequence(sequence);
		return this;
	}

	/**
	 * The timestamp this entry occurred at.
	 */
	public DateTime timestamp() {

		if (timestamp == null && message.hasTimeStamp()) {

			final DateTimeValue dt = ProtoDateUtil.fromDecimalDateTime(message.getTimeStamp());

			if (dt != null) {
				timestamp = new DateTime(dt.getYear(), dt.getMonth(), dt.getDay(), dt.getHour(), dt.getMinute(),
						dt.getSecond(), dt.getMillis(), ISOChronology.getInstanceUTC());
			}

		}

		return timestamp;

	}

	public MarketStateEntry timestamp(final DateTime timestamp) {

		if (timestamp != null) {
			builder().setTimeStamp(ProtoDateUtil.intoDecimalDateTime(
					timestamp.getYear(),
					timestamp.getMonthOfYear(),
					timestamp.getDayOfMonth(),
					timestamp.getHourOfDay(),
					timestamp.getMinuteOfHour(),
					timestamp.getSecondOfMinute(),
					timestamp.getMillisOfSecond()));

			this.timestamp = timestamp;
		}

		return this;

	}

	public MarketStateEntry timestamp(final long timestamp) {
		return timestamp(new DateTime(timestamp, ISOChronology.getInstanceUTC()));
	}

	/**
	 * The trade date / session this entry applies to.
	 */
	public LocalDate tradeDate() {

		if (tradeDate == null) {

			final DateOnlyValue dv = ProtoDateUtil.fromDecimalDateOnly(message.getTradeDate());

			tradeDate = new LocalDate(dv.getYear(), dv.getMonth(), dv.getDay());

		}

		return tradeDate;

	}

	public MarketStateEntry tradeDate(final LocalDate tradeDate) {

		builder().setTradeDate(ProtoDateUtil.intoDecimalDateOnly(tradeDate.getYear(),
				tradeDate.getMonthOfYear(), tradeDate.getDayOfMonth()));

		this.tradeDate = tradeDate;

		return this;

	}

	public MarketStateEntry tradeDate(final long timestamp) {
		return tradeDate(new LocalDate(timestamp, ISOChronology.getInstanceUTC()));
	}

	/**
	 * The entry price.
	 */
	public Price price() {

		if (price == null) {

			if (message.hasPriceMantissa()) {
				price = VALUES.newPrice(message.getPriceMantissa(), message.getPriceExponent());
			} else {
				price = Price.NULL;
			}

		}

		return price;

	}

	public MarketStateEntry price(final Price price) {

		if (price.isNull()) {
			builder().clearPriceMantissa();
			builder().clearPriceExponent();
		} else {
			builder().setPriceMantissa(price.mantissa());
			builder().setPriceExponent(price.exponent());
		}

		this.price = price;

		return this;

	}

	/**
	 * The entry size.
	 */
	public Size size() {

		if (size == null) {

			if (message.hasSizeMantissa()) {
				size = VALUES.newSize(message.getSizeMantissa(), message.getSizeExponent());
			} else {
				size = Size.NULL;
			}

		}

		return size;

	}

	public MarketStateEntry size(final Size size) {

		if (size.isNull()) {
			builder().clearSizeMantissa();
			builder().clearSizeExponent();
		} else {
			builder().setSizeMantissa(size.mantissa());
			builder().setSizeExponent(size.exponent());
		}

		this.size = size;

		return this;

	}

	/**
	 * The position of this entry in the order book (market depth message types only.)
	 */
	public long index() {
		return message.getIndex();
	}

	public MarketStateEntry index(final long idx) {
		builder().setIndex(idx);
		return this;
	}

	/**
	 * The order ID for this entry (market depth message types only.)
	 */
	public long orderId() {
		return message.getOrderId();
	}

	public MarketStateEntry orderId(final long orderId) {
		builder().setOrderId(orderId);
		return this;
	}

	/**
	 * The order count for this entry (market depth message types only.)
	 */
	public int orderCount() {
		return message.getOrderCount();
	}

	public MarketStateEntry orderCount(final int count) {
		builder().setOrderCount(count);
		return this;
	}

}
