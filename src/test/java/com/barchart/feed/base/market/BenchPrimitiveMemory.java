/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.market;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.market.provider.DefBookEntry;
import com.barchart.feed.base.market.provider.DefMarket;
import com.barchart.feed.base.market.provider.VarMarket;
import com.barchart.util.bench.size.JavaSize;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.provider.ValueBuilder;

public class BenchPrimitiveMemory {

	private static final Logger log = LoggerFactory
			.getLogger(BenchPrimitiveMemory.class);

	static int COUNT = 100 * 1000;

	static PriceValue[] priceArray = new PriceValue[COUNT];
	static SizeValue[] sizeArray = new SizeValue[COUNT];
	static TimeValue[] timeArray = new TimeValue[COUNT];
	static String[] stringArray = new String[COUNT];
	static Lock[] lockArray = new ReentrantLock[COUNT];
	static Object[] objectArray = new Object[COUNT];
	static DefMarket[] marketArray = new DefMarket[COUNT];
	static VarMarket[] marketArray2 = new VarMarket[COUNT];

	static BenchClassByte[] benchByte = new BenchClassByte[COUNT];
	static BenchClassInt[] benchInt = new BenchClassInt[COUNT];
	static BenchClassLong[] benchLong = new BenchClassLong[COUNT];

	static BenchClassByteLong[] benchByteLong = new BenchClassByteLong[COUNT];
	static BenchClassLongLong[] benchLongLong = new BenchClassLongLong[COUNT];

	static BenchClassByteLongLong[] benchByteLongLong = new BenchClassByteLongLong[COUNT];

	static BenchClassByteExt[] benchByteExt = new BenchClassByteExt[COUNT];
	static BenchClassByteExtExt[] benchByteExtExt = new BenchClassByteExtExt[COUNT];

	static DefBookEntry[] bookEntryArray = new DefBookEntry[COUNT];

	public static void main(final String[] args) throws Exception {

		log.info("PriceValue {}", JavaSize.of(ValueBuilder.newPrice(0, 0)));
		log.info("SizeValue {}", JavaSize.of(ValueBuilder.newSize(0)));
		log.info("TimeValue {}", JavaSize.of(ValueBuilder.newTime(0)));
		log.info("String {}", JavaSize.of(new String()));
		log.info("ReentrantLock {}", JavaSize.of(new ReentrantLock()));
		log.info("Object {}", JavaSize.of(new Object()));
		log.info("DefMarket {}", JavaSize.of(new DefMarket()));
		log.info("VarMarketL1 {}", JavaSize.of(new MockVarMarket()));

		log.info("BenchClassByte {}", JavaSize.of(new BenchClassByte()));
		log.info("BenchClassInt {}", JavaSize.of(new BenchClassInt()));
		log.info("BenchClassLong {}", JavaSize.of(new BenchClassLong()));

		log.info("BenchClassByteLong {}", JavaSize.of(new BenchClassByteLong()));
		log.info("BenchClassLongLong {}", JavaSize.of(new BenchClassLongLong()));
		log.info("BenchClassByteLongLong {}",
				JavaSize.of(new BenchClassByteLongLong()));

		log.info("BenchClassByteExt {}", JavaSize.of(new BenchClassByteExt()));
		log.info("BenchClassByteExtExt {}",
				JavaSize.of(new BenchClassByteExtExt()));

		// log.info("BookEntry {}", JavaSize.of(new BookEntry()));

		for (int k = 0; k < COUNT; k++) {

			priceArray[k] = ValueBuilder.newPrice(k, 123);
			sizeArray[k] = ValueBuilder.newSize(k);
			timeArray[k] = ValueBuilder.newTime(k);
			stringArray[k] = new String();
			lockArray[k] = new ReentrantLock();
			objectArray[k] = new Object();
			marketArray[k] = new DefMarket();
			marketArray2[k] = new MockVarMarket();

			benchByte[k] = new BenchClassByte();
			benchInt[k] = new BenchClassInt();
			benchLong[k] = new BenchClassLong();

			benchByteLong[k] = new BenchClassByteLong();
			benchLongLong[k] = new BenchClassLongLong();

			benchByteLongLong[k] = new BenchClassByteLongLong();

			benchByteExt[k] = new BenchClassByteExt();
			benchByteExtExt[k] = new BenchClassByteExtExt();

			// bookEntryArray[k] = new BookEntry();

		}

		System.gc();

		log.info("READY");

		Thread.sleep(1000 * 1000);

	}

}
