package com.barchart.feed.series;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import com.barchart.feed.api.series.Period;
import com.barchart.util.value.ValueFactoryImpl;

public class SpanImplTest {
    private static final ValueFactoryImpl FACTORY = new ValueFactoryImpl();

    @Test
    public void testUnion() {
		final DateTime t = new DateTime(2013, 12, 11, 8, 0, 0);
		final DateTime t2 = new DateTime(2013, 12, 11, 13, 0, 0);
        final SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);

		final DateTime t3 = new DateTime(2013, 12, 11, 12, 0, 0);
		final DateTime t4 = new DateTime(2013, 12, 11, 16, 0, 0);
        final SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);

        final SpanImpl union = newSpan.union(newSpan2);
		assertEquals(t, union.getDate());
		assertEquals(t4, union.getNextDate());
    }

    @Test
    public void testIntersection() {
		final DateTime t = new DateTime(2013, 12, 11, 8, 0, 0);
		final DateTime t2 = new DateTime(2013, 12, 11, 13, 0, 0);
        final SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);

		final DateTime t3 = new DateTime(2013, 12, 11, 12, 0, 0);
		final DateTime t4 = new DateTime(2013, 12, 11, 16, 0, 0);
        final SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);

        final SpanImpl inter = newSpan.intersection(newSpan2);
		assertEquals(t3, inter.getDate());
		assertEquals(t2, inter.getNextDate());
    }

    @Test
    public void testExtendsSpan() {
		final DateTime t = new DateTime(2013, 12, 11, 8, 0, 0);
		final DateTime t2 = new DateTime(2013, 12, 11, 13, 0, 0);
        final SpanImpl newSpan = new SpanImpl(Period.ONE_HOUR, t, t2);

		final DateTime t3 = new DateTime(2013, 12, 11, 12, 0, 0);
		final DateTime t4 = new DateTime(2013, 12, 11, 16, 0, 0);
        final SpanImpl newSpan2 = new SpanImpl(Period.ONE_HOUR, t3, t4);

        assertTrue(newSpan2.extendsSpan(newSpan) && newSpan.extendsSpan(newSpan2));
    }
}
