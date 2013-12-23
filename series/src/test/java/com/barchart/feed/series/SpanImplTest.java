package com.barchart.feed.series;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.temporal.Period;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;

public class SpanImplTest {

    @Test
    public void testUnion() {
        Time t = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 8, 0, 0).getMillis());
        Time t2 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 13, 0, 0).getMillis());
        SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);
        
        Time t3 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 12, 0, 0).getMillis());
        Time t4 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 16, 0, 0).getMillis());
        SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);
        
        SpanImpl union = newSpan.union(newSpan2);
        assertEquals(t, union.getTime());
        assertEquals(t4, union.getNextTime());
    }
    
    @Test
    public void testIntersection() {
        Time t = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 8, 0, 0).getMillis());
        Time t2 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 13, 0, 0).getMillis());
        SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);
        
        Time t3 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 12, 0, 0).getMillis());
        Time t4 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 16, 0, 0).getMillis());
        SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);
        
        SpanImpl inter = newSpan.intersection(newSpan2);
        assertEquals(t3, inter.getTime());
        assertEquals(t2, inter.getNextTime());
    }

    @Test
    public void testExtendsSpan() {
        Time t = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 8, 0, 0).getMillis());
        Time t2 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 13, 0, 0).getMillis());
        SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);
        
        Time t3 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 12, 0, 0).getMillis());
        Time t4 = ValueFactoryImpl.factory.newTime(new DateTime(2013, 12, 11, 16, 0, 0).getMillis());
        SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);
        
        assertTrue(newSpan2.extendsSpan(newSpan) && newSpan.extendsSpan(newSpan2));
    }
}
