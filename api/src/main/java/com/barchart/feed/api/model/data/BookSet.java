package com.barchart.feed.api.model.data;

import com.barchart.feed.api.model.data.Book.Type;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface BookSet extends MarketData<BookSet> {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	Book book(Book.Type type);
	
	@Override
	Instrument instrument();

	@Override
	Time updated();

	@Override
	BookSet freeze();
	
	@Override
	boolean isNull();
	
	BookSet NULL = new BookSet() {

		@Override
		public Book book(Type type) {
			return Book.NULL;
		}

		@Override
		public Instrument instrument() {
			return Instrument.NULL;
		}

		@Override
		public Time updated() {
			return Time.NULL;
		}

		@Override
		public BookSet freeze() {
			return this;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
