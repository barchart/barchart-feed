package com.barchart.feed.api.model.data;

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
			throw new UnsupportedOperationException();
		}

		@Override
		public Instrument instrument() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Time updated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
		@Override
		public String toString() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Set<Type> change() {
			throw new UnsupportedOperationException();
		}
		
	};

}
