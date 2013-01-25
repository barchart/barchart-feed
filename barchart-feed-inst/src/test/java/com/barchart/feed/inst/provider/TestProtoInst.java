package com.barchart.feed.inst.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.barchart.proto.buf.inst.BookType;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstType;
import com.barchart.proto.buf.inst.Instrument;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceDisplay;
import com.barchart.proto.buf.inst.PriceFraction;
import com.google.protobuf.ExtensionRegistry;

public class TestProtoInst {
	
	public static void main(final String[] args) throws IOException {
		
		PriceDisplay.Builder priceDisplayBuilder = PriceDisplay.newBuilder();
		priceDisplayBuilder.setPriceFactor(100);
		priceDisplayBuilder.setFraction(PriceFraction.FactionBinary_N01);
		
		Interval.Builder intervalBuilder = Interval.newBuilder();
		intervalBuilder.setTimeStart(0);
		intervalBuilder.setTimeFinish(24);
		
		Calendar.Builder calBuilder = Calendar.newBuilder();
		calBuilder.setLifeTime(intervalBuilder.build());
		//calBuilder.setMarketHours(1, intervalBuilder.build());
		
		Instrument.Builder instBuilder = Instrument.newBuilder();
		instBuilder.setSourceId("TestSource");
		instBuilder.setTargetId(1234);
		instBuilder.setInstType(InstType.FutureInst);
		instBuilder.setBookType(BookType.DefaultBook);
		instBuilder.setBookSize(10);
		instBuilder.setSymbol("TestSymbol");
		instBuilder.setDescription("TestDesc");
		instBuilder.setPriceDisplay(priceDisplayBuilder.build());
		instBuilder.setCalendar(calBuilder.build());
		instBuilder.setCodeCFI("TestCodeCFI");
		instBuilder.setCurrency("TestCurrency");
		instBuilder.setRecordCreateTime(1234);
		instBuilder.setRecordUpdateTime(1234);
		
		Instrument instrument = instBuilder.build();
		
		//write to file
		File test = new File("src/test/resources/test.txt");
		
		OutputStream outStream = new FileOutputStream(test);
		instrument.writeDelimitedTo(outStream);
		instrument.writeDelimitedTo(outStream);
		outStream.close();
		
		InputStream inStream = new FileInputStream(test);
		ExtensionRegistry registry = ExtensionRegistry.newInstance();
		Instrument testRead = Instrument.parseDelimitedFrom(inStream, registry);
		System.out.println(testRead.toString());
		
		while(testRead != null) {
			testRead = Instrument.parseDelimitedFrom(inStream, registry);
			System.out.println(testRead.toString());
		}
		inStream.close();
		
		
		
	}

}
