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
			return null;
		}

		@Override
		public DateTime stop() {
			return null;
		}

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
