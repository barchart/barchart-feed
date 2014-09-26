package com.barchart.feed.openfeed.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.openfeed.AggregationPeriod;
import org.openfeed.MarketEntry;
import org.openfeed.MarketEntry.Descriptor;
import org.openfeed.MarketEntry.Type;
import org.openfeed.MarketEntryOrBuilder;
import org.openfeed.MarketHistoricalSnapshot;
import org.openfeed.MarketHistoricalSnapshot.Builder;
import org.openfeed.MarketHistoricalSnapshotOrBuilder;
import org.openfeed.util.datetime.DateOnlyValue;
import org.openfeed.util.datetime.DateTimeValue;
import org.openfeed.util.datetime.ProtoDateUtil;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/**
 * Wrapper around a MarketSnapshot protobuf message to for simpler usage and more efficient repeated field access.
 *
 * TODO This should be moved into barchart-feed at some point
 */
public class MarketHistoricalState {

	private final MarketHistoricalSnapshotOrBuilder message;
	private final Entries entries;
	private DateTime timestamp;
	private LocalDate tradeDate;

	/**
	 * Construct a new wrapper with a mutable message builder.
	 */
	public MarketHistoricalState() {
		this(MarketHistoricalSnapshot.newBuilder());
	}

	/**
	 * Construct a new wrapper with the given message or builder.
	 */
	public MarketHistoricalState(final MarketHistoricalSnapshotOrBuilder message_) {
		message = message_;
		entries = new Entries();
	}

	/**
	 * Build an immutable protobuf message from the current message or builder. If the underlying message was already
	 * built, it will be returned instead of copied.
	 */
	public MarketHistoricalSnapshot message() {

		if (message instanceof MarketHistoricalSnapshot)
			return (MarketHistoricalSnapshot) message;

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
	 * Globally unique market identifier.
	 */
	public long id() {
		return message.getBaseMarketId();
	}

	public MarketHistoricalState id(final long id) {
		builder().setBaseMarketId(id);
		return this;
	}

	public long sequence() {
		return message.getBaseSequence();
	}

	public MarketHistoricalState sequence(final long sequence) {
		builder().setBaseSequence(sequence);
		return this;
	}

	/**
	 * The timestamp for this message.
	 */
	public DateTime timestamp() {

		if (timestamp == null) {

			final DateTimeValue dt = ProtoDateUtil.fromDecimalDateTime(message.getBaseTimeStamp());

			timestamp = new DateTime(dt.getYear(), dt.getMonth(), dt.getDay(), dt.getHour(), dt.getMinute(),
					dt.getSecond(), dt.getMillis(), ISOChronology.getInstanceUTC());

		}

		return timestamp;

	}

	public MarketHistoricalState timestamp(final DateTime timestamp) {

		builder().setBaseTimeStamp(ProtoDateUtil.intoDecimalDateTime(
				timestamp.getYear(),
				timestamp.getMonthOfYear(),
				timestamp.getDayOfMonth(),
				timestamp.getHourOfDay(),
				timestamp.getMinuteOfHour(),
				timestamp.getSecondOfMinute(),
				timestamp.getMillisOfSecond()));

		this.timestamp = timestamp;

		return this;

	}

	public MarketHistoricalState timestamp(final long timestamp) {
		return timestamp(new DateTime(timestamp, ISOChronology.getInstanceUTC()));
	}

	/**
	 * The trade date for this message.
	 */
	public LocalDate tradeDate() {

		if (tradeDate == null) {

			final DateOnlyValue dv = ProtoDateUtil.fromDecimalDateOnly(message.getBaseTradeDate());

			tradeDate = new LocalDate(dv.getYear(), dv.getMonth(), dv.getDay());

		}

		return tradeDate;

	}

	public MarketHistoricalState tradeDate(final LocalDate tradeDate) {

		builder().setBaseTradeDate(ProtoDateUtil.intoDecimalDateOnly(tradeDate.getYear(),
				tradeDate.getMonthOfYear(), tradeDate.getDayOfMonth()));

		this.tradeDate = tradeDate;

		return this;

	}

	public MarketHistoricalState tradeDate(final long timestamp) {
		return tradeDate(new LocalDate(timestamp, ISOChronology.getInstanceUTC()));
	}

	/**
	 * The default price exponent.
	 */
	public long priceExponent() {
		return message.getBasePriceExponent();
	}

	public MarketHistoricalState priceExponent(final int exponent) {
		builder().setBasePriceExponent(exponent);
		return this;
	}

	/**
	 * The default size exponent.
	 */
	public long sizeExponent() {
		return message.getBaseSizeExponent();
	}

	public MarketHistoricalState sizeExponent(final int exponent) {
		builder().setBaseSizeExponent(exponent);
		return this;
	}

	/**
	 * The last update sequence number.
	 */
	public long lastUpdateSequence() {
		return message.getLastUpdateSequence();
	}

	public MarketHistoricalState lastUpdateSequence(final long sequence) {
		builder().setLastUpdateSequence(sequence);
		return this;
	}

	/**
	 * The number of entries expected for this snapshot (can span multiple messages).
	 */
	public int expectedEntries() {
		return message.getTotalExpectedEntries();
	}

	public MarketHistoricalState expectedEntries(final int expected) {
		builder().setTotalExpectedEntries(expected);
		return this;
	}

	/**
	 * The aggregation period for this bar.
	 */
	public AggregationPeriod aggregation() {
		return message.getAggregation();
	}

	public MarketHistoricalState aggregation(final AggregationPeriod period) {
		builder().setAggregation(period);
		return this;
	}

	/**
	 * The aggregation period for this bar.
	 */
	public int periodCount() {
		return message.getPeriodCount();
	}

	public MarketHistoricalState periodCount(final int count) {
		builder().setPeriodCount(count);
		return this;
	}

	/**
	 * Get all market entries.
	 */
	public Iterable<MarketStateEntry> entries() {
		return entries;
	}

	/**
	 * Get the first market entry with the specified type.
	 */
	public MarketStateEntry entryOrNew(final MarketEntry.Type type) {

		MarketStateEntry entry = entries.one(type);

		if (entry == null) {
			entry = new MarketStateEntry().type(type);
			entries.add(entry);
		}

		return entry;

	}

	/**
	 * Get the first market entry with the specified type.
	 */
	public MarketStateEntry entry(final MarketEntry.Type type) {
		return entries.one(type);
	}

	/**
	 * Get all market entries with the specified type.
	 */
	public List<MarketStateEntry> entries(final MarketEntry.Type type) {
		return entries.all(type);
	}

	/**
	 * Get the first market entry with the specified type.
	 */
	public MarketStateEntry entryOrNew(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {

		MarketStateEntry entry = entries.one(type, descriptor);

		if (entry == null) {
			entry = new MarketStateEntry().type(type).addDescriptor(descriptor);
			entries.add(entry);
		}

		return entry;

	}

	/**
	 * Get the first market entry with the specified type and descriptor.
	 */
	public MarketStateEntry entry(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {
		return entries.one(type, descriptor);
	}

	/**
	 * Get all market entries with the specified type and descriptor.
	 */
	public List<MarketStateEntry> entries(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {
		return entries.all(type, descriptor);
	}

	/**
	 * Add a market entry to the snapshot.
	 */
	public MarketHistoricalState entry(final MarketStateEntry entry) {

		final MarketEntryOrBuilder eob = entry.message;

		if (eob instanceof MarketEntry.Builder) {
			builder().addEntry((MarketEntry.Builder) eob);
		} else if (eob instanceof MarketEntry) {
			builder().addEntry((MarketEntry) eob);
		}

		entries.add(entry);

		return this;

	}

	// Shortcut methods

	public Price price(final MarketEntry.Type type) {

		final MarketStateEntry entry = entry(type);

		if (entry != null) {
			return entry.price();
		}

		return Price.NULL;

	}

	public Price price(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {

		final MarketStateEntry entry = entry(type, descriptor);

		if (entry != null) {
			return entry.price();
		}

		return Price.NULL;

	}

	public Size size(final MarketEntry.Type type) {

		final MarketStateEntry entry = entry(type);

		if (entry != null) {
			return entry.size();
		}

		return Size.NULL;

	}

	public Size size(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {

		final MarketStateEntry entry = entry(type, descriptor);

		if (entry != null) {
			return entry.size();
		}

		return Size.NULL;

	}

	public DateTime timestamp(final MarketEntry.Type type) {

		final MarketStateEntry entry = entry(type);

		if (entry != null) {
			return entry.timestamp();
		}

		return null;

	}

	public DateTime timestamp(final MarketEntry.Type type, final MarketEntry.Descriptor descriptor) {

		final MarketStateEntry entry = entry(type, descriptor);

		if (entry != null) {
			return entry.timestamp();
		}

		return null;

	}

	protected class Entries implements Iterable<MarketStateEntry> {

		protected final Map<Type, List<MarketStateEntry>> entries =
				new EnumMap<Type, List<MarketStateEntry>>(Type.class);

		protected Entries() {
			for (final MarketEntry entry : message.getEntryList()) {
				add(new MarketStateEntry(entry));
			}
		}

		@Override
		public Iterator<MarketStateEntry> iterator() {

			final Iterator<List<MarketStateEntry>> allIter = entries.values().iterator();

			return new Iterator<MarketStateEntry>() {

				Iterator<MarketStateEntry> entryIter = allIter.next().iterator();
				MarketStateEntry next = entryIter.next();

				@Override
				public boolean hasNext() {
					return next != null;
				}

				@Override
				public MarketStateEntry next() {

					if (next == null)
						throw new NoSuchElementException();

					final MarketStateEntry current = next;

					if (entryIter.hasNext()) {
						next = entryIter.next();
					} else if (allIter.hasNext()) {
						entryIter = allIter.next().iterator();
						next = entryIter.next();
					} else {
						next = null;
					}

					return current;

				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

			};

		}

		protected void add(final MarketStateEntry wrapper) {

			List<MarketStateEntry> list = entries.get(wrapper.type());

			if (list == null) {
				list = new ArrayList<MarketStateEntry>();
				entries.put(wrapper.type(), list);
			}

			list.add(wrapper);

		}

		public MarketStateEntry one(final Type type) {

			if (entries.containsKey(type)) {
				return entries.get(type).get(0);
			}

			return null;

		}

		public List<MarketStateEntry> all(final Type type) {

			if (entries.containsKey(type)) {
				return entries.get(type);
			}

			return Collections.emptyList();

		}

		public MarketStateEntry one(final Type type, final Descriptor descriptor) {

			if (entries.containsKey(type)) {
				for (final MarketStateEntry wrapper : entries.get(type)) {
					if (wrapper.descriptors().contains(descriptor))
						return wrapper;
				}
			}

			return null;

		}

		public List<MarketStateEntry> all(final Type type, final Descriptor descriptor) {

			final List<MarketStateEntry> wrappers = new ArrayList<MarketStateEntry>();

			if (entries.containsKey(type)) {
				for (final MarketStateEntry wrapper : entries.get(type)) {
					if (wrapper.descriptors().contains(descriptor))
						wrappers.add(wrapper);
				}
			}

			return wrappers;

		}

	}

}