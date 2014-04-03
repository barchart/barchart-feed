package com.barchart.feed.series;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.util.value.ValueFactoryImpl;

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

        final DateTime dt = new DateTime(2013, 12, 10, 9, 0, 0, 0);
        final DateTime dt2 = new DateTime(2013, 12, 10, 9, 0, 0, 3);
        final DateTime dt3 = new DateTime(2013, 12, 10, 9, 0, 0, 5);
        final DateTime dt4 = new DateTime(2013, 12, 10, 9, 0, 0, 7);
        final DateTime dt5 = new DateTime(2013, 12, 10, 9, 0, 0, 9);
        final DateTime dt6 = new DateTime(2013, 12, 10, 9, 0, 0, 11);
        final DateTime dt7 = new DateTime(2013, 12, 10, 9, 0, 0, 13);
        final DateTime dt8 = new DateTime(2013, 12, 10, 9, 0, 0, 15);
        final DateTime dt9 = new DateTime(2013, 12, 10, 9, 0, 1, 0);
        final DateTime dt10 = new DateTime(2013, 12, 10, 9, 0, 1, 2);
        final DateTime dt11 = new DateTime(2013, 12, 10, 9, 0, 1, 4);
        final DateTime dt12 = new DateTime(2013, 12, 10, 9, 0, 1, 6);
        final DateTime dt13 = new DateTime(2013, 12, 10, 9, 0, 2, 0);
        final DateTime dt14 = new DateTime(2013, 12, 10, 9, 0, 2, 2);
        final DateTime dt15 = new DateTime(2013, 12, 10, 9, 0, 2, 4);
        final DateTime dt16 = new DateTime(2013, 12, 10, 12, 0, 0, 147);

        final DateTime t = new DateTime(2013, 12, 10, 11, 59, 58, 1);

        final Period p = new Period(PeriodType.TICK, 1);
        final DataSeriesImpl<DataPointImpl> ds = new DataSeriesImpl<DataPointImpl>(p);

		ds.insertData(new BarImpl(null, dt, p, null, null, null, null, null, null));
        assertEquals(1, ds.size());
		assertEquals(0, ds.indexOf(dt, false));

		ds.insertData(new BarImpl(null, dt2, p, null, null, null, null, null, null));
        assertEquals(2, ds.size());
		assertEquals(1, ds.indexOf(dt2, false));

		ds.insertData(new BarImpl(null, dt3, p, null, null, null, null, null, null));
        assertEquals(3, ds.size());
		assertEquals(2, ds.indexOf(dt3, false));

		ds.insertData(new BarImpl(null, dt4, p, null, null, null, null, null, null));
        assertEquals(4, ds.size());
		assertEquals(3, ds.indexOf(dt4, false));

		ds.insertData(new BarImpl(null, dt5, p, null, null, null, null, null, null));
        assertEquals(5, ds.size());
		assertEquals(4, ds.indexOf(dt5, false));

		ds.insertData(new BarImpl(null, dt6, p, null, null, null, null, null, null));
        assertEquals(6, ds.size());
		assertEquals(5, ds.indexOf(dt6, false));

		ds.insertData(new BarImpl(null, dt7, p, null, null, null, null, null, null));
        assertEquals(7, ds.size());
		assertEquals(6, ds.indexOf(dt7, false));

		ds.insertData(new BarImpl(null, dt8, p, null, null, null, null, null, null));
        assertEquals(8, ds.size());
		assertEquals(7, ds.indexOf(dt8, false));

		ds.insertData(new BarImpl(null, dt9, p, null, null, null, null, null, null));
        assertEquals(9, ds.size());
		assertEquals(8, ds.indexOf(dt9, false));

		ds.insertData(new BarImpl(null, dt10, p, null, null, null, null, null, null));
        assertEquals(10, ds.size());
		assertEquals(9, ds.indexOf(dt10, false));

		ds.insertData(new BarImpl(null, dt11, p, null, null, null, null, null, null));
        assertEquals(11, ds.size());
		assertEquals(10, ds.indexOf(dt11, false));

		ds.insertData(new BarImpl(null, dt12, p, null, null, null, null, null, null));
        assertEquals(12, ds.size());
		assertEquals(11, ds.indexOf(dt12, false));

		ds.insertData(new BarImpl(null, dt13, p, null, null, null, null, null, null));
        assertEquals(13, ds.size());
		assertEquals(12, ds.indexOf(dt13, false));

		ds.insertData(new BarImpl(null, dt14, p, null, null, null, null, null, null));
        assertEquals(14, ds.size());
		assertEquals(13, ds.indexOf(dt14, false));

		ds.insertData(new BarImpl(null, dt15, p, null, null, null, null, null, null));
        assertEquals(15, ds.size());
		assertEquals(14, ds.indexOf(dt15, false));

		ds.insertData(new BarImpl(null, dt16, p, null, null, null, null, null, null));
        assertEquals(16, ds.size());
		assertEquals(15, ds.indexOf(dt16, false));

        ////

        //Becomes index 15 and bumps the above insertion to the right
		ds.insertData(new BarImpl(null, t, p, null, null, null, null, null, null));
        assertEquals(17, ds.size());
		assertEquals(15, ds.indexOf(t, false));

        //Prev dt16 now is moved to the right due to the above insertion
		assertEquals(16, ds.indexOf(dt16, false));

        System.out.println(dt);
    }

}
