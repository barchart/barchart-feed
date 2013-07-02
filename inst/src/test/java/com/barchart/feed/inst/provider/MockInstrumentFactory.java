package com.barchart.feed.inst.provider;

import static org.mockito.Mockito.*;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifier;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;

public class MockInstrumentFactory {

	private static final Factory valFactory = FactoryLoader.load();
	
	public static Instrument newInstrument() {
		
		final Instrument inst = mock(Instrument.class);
		
		when(inst.id()).thenReturn(Identifier.NULL);
		
		return inst;
		
	}
	
	public static Instrument newInstrument(final String id) {
		
		final Instrument inst = mock(Instrument.class);
		
		when(inst.id()).thenReturn(new Identifier(){

			final String thisID = id.intern();
			
			@Override
			public int compareTo(Identifier o) {
				return id.compareTo(o.toString());
			}
			
			@Override
			public String toString() {
				return thisID;
			}
			
			@Override
			public boolean isNull () {
				return this == Identifier.NULL;
			}
			
		});
		
		return inst;
		
	}
	
}
