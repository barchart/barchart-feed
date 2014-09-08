package com.barchart.feed.api.model.meta.instrument;

import org.joda.time.DateTime;

import com.barchart.util.value.api.Existential;

/**
 * An instance of a trading event.
 */
public interface Event extends Existential {

	/**
	 * The event type.
	 */
	public enum Type {

		UNKNOWN("Unknown"),

		// All instruments
		FIRST_TRADE_DATE("First Trade Date"),
		LAST_TRADE_DATE("Last Trade Date"),

		// Futures only
		SETTLEMENT_DATE("Settlement Date"),
		FIRST_DELIVERY_DATE("First Delivery Date"),
		LAST_DELIVERY_DATE("Last Delivery Date"),
		FIRST_NOTICE_DATE("First Notice Date"),
		LAST_NOTICE_DATE("Last Notice Date"),
		FIRST_HOLDING_DATE("First Holding Date"),
		LAST_HOLDING_DATE("Last Holding Date"),
		FIRST_POSITION_DATE("First Position Date"),
		LAST_POSITION_DATE("Last Position Date");

		private final String description;

		private Type(final String desc) {
			description = desc;
		}

		/**
		 * The default event type description.
		 */
		public String description() {
			return description;
		}

	}

	/**
	 * The event type.
	 */
	Type type();

	/**
	 * A description of the event.
	 */
	String description();

	/**
	 * The date that this event takes place on.
	 */
	DateTime date();

	static Event NULL = new Event() {

		@Override
		public boolean isNull() {
			return true;
		}

		@Override
		public Type type() {
			throw new UnsupportedOperationException("NULL Event");
		}

		@Override
		public String description() {
			throw new UnsupportedOperationException("NULL Event");
		}

		@Override
		public DateTime date() {
			throw new UnsupportedOperationException("NULL Event");
		}

	};

}