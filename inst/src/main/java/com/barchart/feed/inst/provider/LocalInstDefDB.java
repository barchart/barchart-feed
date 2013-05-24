/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.inst.provider;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.consumer.data.Instrument;
import com.barchart.feed.api.consumer.inst.InstrumentGUID;
import com.barchart.feed.api.framework.data.InstrumentEntity;
import com.barchart.feed.api.framework.inst.MetadataContext;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

public class LocalInstDefDB implements MetadataContext {
	
	private static final Logger log = LoggerFactory
			.getLogger(LocalInstDefDB.class);

	private final Database db;
	private final Environment env;
	
	public LocalInstDefDB(final String location) {
		
		// Manage directory creation
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
    	envConfig.setAllowCreate(true);
    	envConfig.setTransactional(true);
    	env = new Environment(new File(location), envConfig);
    	
    	DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(true);
        dbConfig.setSortedDuplicates(false);
        db = env.openDatabase(null, "InstrumentDef", dbConfig);
		
        log.debug("Database currently has {} entries", db.count());
        
	}
	
	@Override
	public Instrument lookup(final InstrumentGUID guid) {
		
		Transaction txn = env.beginTransaction(null, null);
		byte[] key = guid.toString().getBytes(); 
		DatabaseEntry result = new DatabaseEntry();
		db.get(txn, new DatabaseEntry(key), result, null);
		txn.commit();
		
		InstrumentDefinition resInst;
		byte[] resData = result.getData();
		
		if(resData == null || resData.length == 0) {
			return InstrumentEntity.NULL_INSTRUMENT;
		}
		
		try {
			resInst = InstrumentDefinition.parseFrom(result.getData());
		} catch (final InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
		
		return InstrumentFactory.buildFromProtoBuf(resInst);
		
	}
	
	public void store(final InstrumentGUID guid, final InstrumentEntity inst) {
		
		Transaction txn = env.beginTransaction(null, null);
		byte[] key = guid.toString().getBytes(); 
		
		db.put(txn, new DatabaseEntry(key), new DatabaseEntry(
				InstrumentProtoBuilder.buildInstDef(inst).toByteArray()));
		txn.commit();
		
		log.debug("Database currently has {} entries", db.count());
		
	}

}
