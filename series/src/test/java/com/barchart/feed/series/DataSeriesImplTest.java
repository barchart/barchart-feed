package com.barchart.feed.series;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;

public class DataSeriesImplTest {
    private static final ValueFactoryImpl FACTORY = new ValueFactoryImpl();
    
    @Test
    public void testInsertData() {
//        1        2013-12-10T09:00:00.003-06:00
//        2        2013-12-10T09:00:00.005-06:00
//        3        2013-12-10T09:00:00.007-06:00
//        4        2013-12-10T09:00:00.009-06:00
//        5        2013-12-10T09:00:00.011-06:00
//        6        2013-12-10T09:00:00.013-06:00
//        7        2013-12-10T09:00:00.015-06:00
//        8        2013-12-10T09:00:01.000-06:00
//        9        2013-12-10T09:00:01.002-06:00
//        10       2013-12-10T09:00:01.004-06:00
//        11       2013-12-10T09:00:01.006-06:00
//        12       2013-12-10T09:00:02.000-06:00
//        13       2013-12-10T09:00:02.002-06:00
//        14       2013-12-10T09:00:02.004-06:00
//        15       2013-12-10T12:00:00.147-06:00
//        2013-12-10T11:59:58.001-06:00 
        
        DateTime dt = new DateTime(2013, 12, 10, 9, 0, 0, 0);
        DateTime dt2 = new DateTime(2013, 12, 10, 9, 0, 0, 3);
        DateTime dt3 = new DateTime(2013, 12, 10, 9, 0, 0, 5);
        DateTime dt4 = new DateTime(2013, 12, 10, 9, 0, 0, 7);
        DateTime dt5 = new DateTime(2013, 12, 10, 9, 0, 0, 9);
        DateTime dt6 = new DateTime(2013, 12, 10, 9, 0, 0, 11);
        DateTime dt7 = new DateTime(2013, 12, 10, 9, 0, 0, 13);
        DateTime dt8 = new DateTime(2013, 12, 10, 9, 0, 0, 15);
        DateTime dt9 = new DateTime(2013, 12, 10, 9, 0, 1, 0);
        DateTime dt10 = new DateTime(2013, 12, 10, 9, 0, 1, 2);
        DateTime dt11 = new DateTime(2013, 12, 10, 9, 0, 1, 4);
        DateTime dt12 = new DateTime(2013, 12, 10, 9, 0, 1, 6);
        DateTime dt13 = new DateTime(2013, 12, 10, 9, 0, 2, 0);
        DateTime dt14 = new DateTime(2013, 12, 10, 9, 0, 2, 2);
        DateTime dt15 = new DateTime(2013, 12, 10, 9, 0, 2, 4);
        DateTime dt16 = new DateTime(2013, 12, 10, 12, 0, 0, 147);
        
        DateTime t = new DateTime(2013, 12, 10, 11, 59, 58, 1);
        
        Period p = new Period(PeriodType.TICK, 1);
        DataSeriesImpl<DataPointImpl> ds = new DataSeriesImpl<DataPointImpl>(p);
        
        Time ti = FACTORY.newTime(dt.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(1, ds.size());
        assertEquals(0, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt2.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(2, ds.size());
        assertEquals(1, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt3.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(3, ds.size());
        assertEquals(2, ds.indexOf(ti, false));
       
        ti = FACTORY.newTime(dt4.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(4, ds.size());
        assertEquals(3, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt5.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(5, ds.size());
        assertEquals(4, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt6.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(6, ds.size());
        assertEquals(5, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt7.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(7, ds.size());
        assertEquals(6, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt8.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(8, ds.size());
        assertEquals(7, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt9.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(9, ds.size());
        assertEquals(8, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt10.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(10, ds.size());
        assertEquals(9, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt11.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(11, ds.size());
        assertEquals(10, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt12.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(12, ds.size());
        assertEquals(11, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt13.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(13, ds.size());
        assertEquals(12, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt14.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(14, ds.size());
        assertEquals(13, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt15.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(15, ds.size());
        assertEquals(14, ds.indexOf(ti, false));
        
        ti = FACTORY.newTime(dt16.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(16, ds.size());
        assertEquals(15, ds.indexOf(ti, false));
        
        ////
        
        //Becomes index 15 and bumps the above insertion to the right
        ti = FACTORY.newTime(t.getMillis());
        ds.insertData(new BarImpl(ti, p, null, null, null, null, null, null));
        assertEquals(17, ds.size());
        assertEquals(15, ds.indexOf(ti, false));
         
        //Prev dt16 now is moved to the right due to the above insertion
        ti = FACTORY.newTime(dt16.getMillis());
        assertEquals(16, ds.indexOf(ti, false));
        
        System.out.println(dt);
    }

}
