package com.barchart.feed.meta.instrument;

import org.joda.time.DateTimeZone;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.id.ExchangeID;

public class DefaultExchange implements Exchange {

	private final ExchangeID id;
	private final String mic;
	private final String description;
	private final String countryCode;
	private final String currencyCode;
	private final DateTimeZone timeZone;
	private final int delay;

	public DefaultExchange(final ExchangeID id_, final String mic_, final String description_,
			final String countryCode_, final String currencyCode_, final DateTimeZone timeZone_, final int delay_) {

		id = id_;
		mic = mic_;
		description = description_;
		countryCode = countryCode_;
		currencyCode = currencyCode_;
		timeZone = timeZone_;
		delay = delay_;

	}

	@Override
	public ExchangeID id() {
		return id;
	}

	@Override
	public String mic() {
		return mic;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String countryCode() {
		return countryCode;
	}

	@Override
	public String currencyCode() {
		return currencyCode;
	}

	@Override
	public DateTimeZone timeZone() {
		return timeZone;
	}

	@Override
	public String timeZoneName() {
		return timeZone.getID();
	}

	@Override
	public int standardDelay() {
		return delay;
	}

	@Override
	public MetaType type() {
		return MetaType.EXCHANGE;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
