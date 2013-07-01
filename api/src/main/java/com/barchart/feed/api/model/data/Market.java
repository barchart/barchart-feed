package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface Market extends MarketData<Market> {

	/** Last trade. */
	Trade trade();

	Book book();

	BookSet bookSet();

	Cuvol cuvol();

	Session session();

	SessionSet sessionSet();

	Market NULL = new Market() {

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
		}

		@Override
		public Time updated() {
			return Time.NULL;
		}

		@Override
		public Trade trade() {
			return Trade.NULL;
		}

		@Override
		public Book book() {
			return Book.NULL;
		}

		@Override
		public BookSet bookSet() {
			return BookSet.NULL;
		}

		@Override
		public Cuvol cuvol() {
			return Cuvol.NULL;
		}

		@Override
		public Session session() {
			return Session.NULL;
		}

		@Override
		public SessionSet sessionSet() {
			return SessionSet.NULL;
		}

		@Override
		public Market freeze() {
			return this;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
		@Override
		public String toString() {
			return "NULL MARKET";
		}

	};

}
