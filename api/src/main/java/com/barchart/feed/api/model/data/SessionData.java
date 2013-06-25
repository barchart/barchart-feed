package com.barchart.feed.api.model.data;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueConst;

public interface SessionData {

	Price open();

	Price high();

	Price low();

	Price close();

	Price settle();

	Size volume();

	Size interest();

	Time timeOpened();

	Time timeUpdated();

	Time timeClosed();

	public static final SessionData NULL_SESSION_DATA = new SessionData() {

		@Override
		public Price open() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price high() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price low() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price close() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Price settle() {
			return ValueConst.NULL_PRICE;
		}

		@Override
		public Size volume() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public Size interest() {
			return ValueConst.NULL_SIZE;
		}

		@Override
		public Time timeOpened() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public Time timeUpdated() {
			return ValueConst.NULL_TIME;
		}

		@Override
		public Time timeClosed() {
			return ValueConst.NULL_TIME;
		}

	};

}
