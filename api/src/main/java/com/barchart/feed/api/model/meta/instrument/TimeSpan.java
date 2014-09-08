package com.barchart.feed.api.model.meta.instrument;

import org.joda.time.DateTime;

import com.barchart.util.value.api.Existential;

public interface TimeSpan extends Existential {

	DateTime start();

	DateTime stop();

	@Override
	boolean isNull();

	TimeSpan NULL = new TimeSpan() {

		@Override
		public DateTime start() {
			throw new UnsupportedOperationException();
		}

		@Override
		public DateTime stop() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
