package com.barchart.feed.inst.provider;

import java.util.Map;

import com.barchart.feed.api.inst.Instrument;
import com.barchart.missive.core.Tag;
import com.barchart.missive.core.TagMap;
import com.barchart.proto.buf.inst.InstrumentDefinition;

public final class InstrumentFactory {
	
	private InstrumentFactory() {
		
	}
	
	public static Instrument buildFromProtoBuf(InstrumentDefinition instDef) {
		return new InstrumentProto(instDef);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Instrument build(final Map<Tag, Object> map) {
		return new InstrumentImpl(map);
	}
	
	public static final Instrument build(final TagMap map) {
		return new InstrumentImpl(map);
	}

}
