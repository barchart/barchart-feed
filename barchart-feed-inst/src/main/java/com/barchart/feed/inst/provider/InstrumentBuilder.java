package com.barchart.feed.inst.provider;

import com.barchart.feed.inst.api.Instrument;

public final class InstrumentBuilder {
	
	private InstrumentBuilder() {
		
	}
	
	public static Instrument buildFromProtoBuf(com.barchart.proto.buf.inst.Instrument instDef) {
		return new InstrumentProto(instDef);
	}

}
