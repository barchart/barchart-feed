package bench;

import java.io.File;
import java.nio.ByteBuffer;

import com.barchart.proto.buf.inst.BookType;
import com.barchart.proto.buf.inst.Calendar;
import com.barchart.proto.buf.inst.InstrumentDefinition;
import com.barchart.proto.buf.inst.Interval;
import com.barchart.proto.buf.inst.PriceDisplay;
import com.barchart.proto.buf.inst.PriceFraction;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;

public class TestBDB {
	
	public static void main(final String[] args) throws InvalidProtocolBufferException {
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);
        Environment env =
                new Environment(new File("src/test/resources/bdb"), envConfig);

        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setTransactional(true);
        dbConfig.setSortedDuplicates(false);
        Database db = env.openDatabase(null, "TestDB", dbConfig);
        
		InstrumentDefinition inst = build();
		byte[] data = inst.toByteArray();
		
		Transaction txn = env.beginTransaction(null, null);
		for(int i = 0; i < 1000000; i++) {
			byte[] key = ByteBuffer.allocate(8).putLong(i).array();
			db.put(txn, new DatabaseEntry(key), new DatabaseEntry(data));
			if(i % 10000 == 0) {
				System.out.println("Stored " + i);
			}
		}
		txn.commit();
		
		long start = System.currentTimeMillis();
		Transaction txn2 = env.beginTransaction(null, null);
		String ID;
		for(int i = 0; i < 1000000; i++) {
			DatabaseEntry result = new DatabaseEntry();
			db.get(txn2, new DatabaseEntry(ByteBuffer.allocate(8).putLong(i).array()), result, null);
			InstrumentDefinition testInst = InstrumentDefinition.parseFrom(result.getData());
			ID = testInst.getVendorSymbol();
			if(i % 10000 == 0) {
				System.out.println("Pulled " + i);
			}
		}
		txn2.commit();
		System.out.println(System.currentTimeMillis() - start);
		
	}
	
	private static InstrumentDefinition build() {
		PriceDisplay.Builder priceDisplayBuilder = PriceDisplay.newBuilder();
		priceDisplayBuilder.setPriceFactor(100);
		priceDisplayBuilder.setFraction(PriceFraction.FactionBinary_N01);
		
		Interval.Builder intervalBuilder = Interval.newBuilder();
		intervalBuilder.setTimeStart(0);
		intervalBuilder.setTimeFinish(24);
		
		Calendar.Builder calBuilder = Calendar.newBuilder();
		calBuilder.setLifeTime(intervalBuilder.build());
		//calBuilder.setMarketHours(1, intervalBuilder.build());
		
		InstrumentDefinition.Builder instBuilder = InstrumentDefinition.newBuilder()
			.setInstrumentId(1234)
			.setBookDepth(10)
			.setVendorSymbol("TestSymbol")
			.setDescription("TestDesc")
			.setCalendar(calBuilder.build())
			.setCurrency("TestCurrency")
			.setRecordCreateTime(1234)
			.setRecordUpdateTime(1234);
		
		return instBuilder.build();
	}

}
