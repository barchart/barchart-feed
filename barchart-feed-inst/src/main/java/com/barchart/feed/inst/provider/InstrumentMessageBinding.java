package com.barchart.feed.inst.provider;

import java.nio.ByteBuffer;

import com.barchart.proto.buf.inst.Instrument;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sleepycat.bind.EntityBinding;
import com.sleepycat.je.DatabaseEntry;

public class InstrumentMessageBinding implements EntityBinding<com.barchart.proto.buf.inst.Instrument> {

	@Override
	public Instrument entryToObject(DatabaseEntry key, DatabaseEntry data) {
		try {
			return Instrument.parseFrom(data.getData());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void objectToKey(Instrument object, DatabaseEntry key) {
		key.setData(ByteBuffer.allocate(8).putLong(object.getTargetId()).array());
	}

	@Override
	public void objectToData(Instrument object, DatabaseEntry data) {
		data.setData(object.toByteArray());
	}

}
