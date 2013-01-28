package com.barchart.feed.inst.provider;

import java.util.Map;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.missive.core.Tag;

public final class InstrumentFactory {
	
	private InstrumentFactory() {
		
	}
	
	public static Instrument buildFromProtoBuf(com.barchart.proto.buf.inst.Instrument instDef) {
		return new InstrumentProto(instDef);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Instrument build(final Map<Tag, Object> map) {
		return new InstrumentImpl(map);
	}

}
