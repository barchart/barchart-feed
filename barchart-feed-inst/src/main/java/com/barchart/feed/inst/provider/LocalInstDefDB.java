package com.barchart.feed.inst.provider;

import java.io.File;
import java.nio.ByteBuffer;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.feed.inst.api.InstrumentConst;
import com.barchart.feed.inst.api.InstrumentGUID;
import com.barchart.feed.inst.api.MetadataContext;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

public class LocalInstDefDB implements MetadataContext {

	private final Database db;
	private final Environment env;
	
	public LocalInstDefDB(final String location) {
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
    	envConfig.setAllowCreate(true);
    	envConfig.setTransactional(true);
    	env = new Environment(new File(location), envConfig);
    	
    	DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(true);
        dbConfig.setSortedDuplicates(false);
        db = env.openDatabase(null, "InstrumentDef", dbConfig);
		
	}
	
	@Override
	public Instrument lookup(final InstrumentGUID guid) {
		
		Transaction txn = env.beginTransaction(null, null);
		byte[] key = ByteBuffer.allocate(8).putLong(guid.getGUID()).array();
		DatabaseEntry result = new DatabaseEntry();
		db.get(txn, new DatabaseEntry(key), result, null);
		txn.commit();
		
		com.barchart.proto.buf.inst.Instrument resInst;
		byte[] resData = result.getData();
		
		if(resData == null || resData.length == 0) {
			return InstrumentConst.NULL_INSTRUMENT;
		}
		
		try {
			resInst = com.barchart.proto.buf.inst.Instrument.parseFrom(result.getData());
		} catch (final InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
		
		return InstrumentBuilder.buildFromProtoBuf(resInst);
		
	}

}
