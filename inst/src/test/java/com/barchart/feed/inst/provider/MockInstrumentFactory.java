package com.barchart.feed.inst.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;

public class MockInstrumentFactory {

	public static Instrument newInstrument() {
		
		final Instrument inst = mock(Instrument.class);
		
		when(inst.id()).thenReturn(InstrumentID.NULL);
		
		return inst;
		
	}
	
	public static Instrument newInstrument(final String id) {
		
		final Instrument inst = mock(Instrument.class);
		
		when(inst.id()).thenReturn(new InstrumentID(id));
		
		return inst;
		
	}
	
}
