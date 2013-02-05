package com.barchart.feed.inst.provider;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.barchart.feed.inst.api.Instrument;
import com.barchart.missive.core.Tag;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.Interval;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;

public class TestInstrumentProto {
	
//	public static void main(final String[] args) throws IOException {
//		
//		PriceDisplay.Builder priceDisplayBuilder = PriceDisplay.newBuilder();
//		priceDisplayBuilder.setPriceFactor(100);
//		priceDisplayBuilder.setFraction(PriceFraction.FactionBinary_N01);
//		
//		Interval.Builder intervalBuilder = Interval.newBuilder();
//		intervalBuilder.setTimeStart(0);
//		intervalBuilder.setTimeFinish(24);
//		
//		Calendar.Builder calBuilder = Calendar.newBuilder();
//		calBuilder.setLifeTime(intervalBuilder.build());
//		//calBuilder.setMarketHours(1, intervalBuilder.build());
//		
//		com.barchart.proto.buf.inst.Instrument.Builder instBuilder = 
//				com.barchart.proto.buf.inst.Instrument.newBuilder();
//		instBuilder.setSourceId("TestSource");
//		instBuilder.setTargetId(1234);
//		instBuilder.setInstType(InstType.FutureInst);
//		instBuilder.setBookType(BookType.DefaultBook);
//		instBuilder.setBookSize(10);
//		instBuilder.setSymbol("TestSymbol");
//		instBuilder.setDescription("TestDesc");
//		instBuilder.setPriceDisplay(priceDisplayBuilder.build());
//		instBuilder.setCalendar(calBuilder.build());
//		instBuilder.setCodeCFI("TestCodeCFI");
//		instBuilder.setCurrency("TestCurrency");
//		instBuilder.setRecordCreateTime(1234);
//		instBuilder.setRecordUpdateTime(1234);
//		
//		com.barchart.proto.buf.inst.Instrument instrument = instBuilder.build();
//		
//		//write to file
//		File test = new File("src/test/resources/test.txt");
//		
//		OutputStream outStream = new FileOutputStream(test);
//		instrument.writeDelimitedTo(outStream);
//		instrument.writeDelimitedTo(outStream);
//		outStream.close();
//		
//		InputStream inStream = new FileInputStream(test);
//		ExtensionRegistry registry = ExtensionRegistry.newInstance();
//		com.barchart.proto.buf.inst.Instrument testRead = com.barchart.proto.buf.inst.
//				Instrument.parseDelimitedFrom(inStream, registry);
//		System.out.println(testRead.toString());
//		
//		while(testRead != null) {
//			testRead = com.barchart.proto.buf.inst.
//					Instrument.parseDelimitedFrom(inStream, registry);
//			System.out.println(testRead.toString());
//		}
//		inStream.close();
//		
//	}
	
	@Test
	public void test() throws InvalidProtocolBufferException {

		//TODO once inst def is finalized
		
//		Instrument protoInst = new InstrumentProto(TestInstrument.TEST_INST_PROTO);
//		
//		Instrument barInst = TestInstrument.TEST_INST_BARCHART;
//		
//		compareInsts(barInst, protoInst);
//		
//		byte[] instBytes = TestInstrument.TEST_INST_PROTO.toByteArray();
//		protoInst = new InstrumentProto(instBytes);
//		
//		compareInsts(barInst, protoInst);
		
	}
	
	public static void compareInsts(final Instrument thisInst, final Instrument thatInst) {
		
		assertTrue(thisInst.size() == thatInst.size());
		
		for(Tag<?> t : thisInst.tags()) {
			assertTrue(thisInst.get(t).equals(thatInst.get(t)));
		}
		
	}

}
