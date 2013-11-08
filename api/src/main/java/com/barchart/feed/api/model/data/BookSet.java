package com.barchart.feed.api.model.data;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.ChangeSet;
import com.barchart.feed.api.model.data.Book.Type;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Time;

public interface BookSet extends MarketData<BookSet>, ChangeSet<Book.Type> {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	Book book(Book.Type type);
	
	@Override
	Set<Type> change();
	
	@Override
	Instrument instrument();

	@Override
	Time updated();

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
		public boolean isNull() {
			return true;
		}
		
		@Override
		public String toString() {
			return "NULL BOOK SET";
		}

		@Override
		public Set<Type> change() {
			return Collections.emptySet();
		}
		
	};

}
