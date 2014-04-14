package com.barchart.feed.api.series;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ZonedChronology;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.RemainderDateTimeField;

/**
 * Mostly snipped from ISOChronology, to provide additional functionality
 * arranged around quarters.
 * <p>
 * <h5><em>
 * This class returns a {@link DurationField} whose only implemented methods
 * (for now) are {@link RangeDurationField#getName()}, {@link RangeDurationField#getDifference(long, long)},
 * and {@link RangeDurationField#add(long, int)}
 * </em></h5>
 * <p>
 * Implements a chronology that follows the rules of the ISO8601 standard,
 * which is compatible with Gregorian for all modern dates.
 * When ISO does not define a field, but it can be determined (such as AM/PM)
 * it is included.
 * <p>
 * With the exception of century related fields, ISOChronology is exactly the
 * same as {@link GregorianChronology}. In this chronology, centuries and year
 * of century are zero based. For all years, the century is determined by
 * dropping the last two digits of the year, ignoring sign. The year of century
 * is the value of the last two year digits.
 * <p>
 * ExtendedChronology is thread-safe and immutable.
 *
 * @author David Ray
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
@SuppressWarnings("serial")
public final class ExtendedChronology extends AssembledChronology {
    
    /** Singleton instance of a UTC ISOChronology */
    private static final ExtendedChronology INSTANCE_UTC;
        
    private static final int FAST_CACHE_SIZE = 64;

    /** Fast cache of zone to chronology */
    private static final ExtendedChronology[] cFastCache;

    /** Cache of zone to chronology */
    private static final Map<DateTimeZone, Chronology> cCache = new HashMap<DateTimeZone, Chronology>();
    static {
        cFastCache = new ExtendedChronology[FAST_CACHE_SIZE];
        INSTANCE_UTC = new ExtendedChronology(GregorianChronology.getInstanceUTC());
        cCache.put(DateTimeZone.UTC, INSTANCE_UTC);
    }

    /**
     * Gets an instance of the ExtendedChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static ExtendedChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the ExtendedChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static ExtendedChronology getInstance() {
        return getInstance(DateTimeZone.getDefault());
    }
    
    /**
     * Gets an instance of the ExtendedChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static ExtendedChronology getInstance(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        int index = System.identityHashCode(zone) & (FAST_CACHE_SIZE - 1);
        ExtendedChronology chrono = cFastCache[index];
        if (chrono != null && chrono.getZone() == zone) {
            return chrono;
        }
        synchronized (cCache) {
            chrono = (ExtendedChronology) cCache.get(zone);
            if (chrono == null) {
                chrono = new ExtendedChronology(ZonedChronology.getInstance(INSTANCE_UTC, zone));
                cCache.put(zone, chrono);
            }
        }
        cFastCache[index] = chrono;
        return chrono;
    }
    
    /**
     * Returns the 1nth based index of the Quarter the specified
     * DateTime resides in.
     * 
     * @param 		dt  the date queried for
     * @return		the specified date's Quarter
     */
    public static int getQuarter(DateTime dt) {
        return (withPeriodStart(dt).getMonthOfYear() + 3) / 3;
    }
    
    /**
     * Returns a new {@link DateTime} whose lesser fields are adjusted to the
     * Quarter period start of the specified DateTime.
     * <em><b>WARNING: Time fields are not preserved and are returned as 00:00:00.000</b></em>
     * @param       dt    the DateTime to be adjusted to the nearest Quarter
     * @return      the DateTime adjusted to the nearest Quarter.
     */
    public static DateTime withPeriodStart(DateTime dt) {
        int div = (dt.getMonthOfYear() % 3);
        int d = dt.getMonthOfYear() - Math.max(0, ((div == 0 ? 3 : div) - 1));
        dt = dt.withMonthOfYear(d);
        long millis = PeriodType.QUARTER.getComparator().getLowerLimit().getField(
        	getInstance()).roundFloor(dt.getMillis());
    	return new DateTime(millis);
    }
    
    /**
     * Returns a new {@link DateTime} whose lesser fields are adjusted to the
     * Quarter period end of the specified DateTime.
     * <em><b>WARNING: Time fields are not preserved and are returned as 00:00:00.000</b></em>
     * @param       dt    the DateTime to be adjusted to the nearest Quarter end
     * @return      the DateTime adjusted to the nearest Quarter end.
     */
    public static DateTime withPeriodEnd(DateTime dt) {
    	dt = withPeriodStart(dt);
    	dt = dt.plusMonths(2).dayOfMonth().withMaximumValue();
    	return dt;
    }
    
    // Constructors and instance variables
    //-----------------------------------------------------------------------

    /**
     * Restricted constructor
     */
    private ExtendedChronology(Chronology base) {
        super(base, null);
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }
    
    public DurationField quarters() {
        return new RangeDurationField();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        String str = "ExtendedChronology";
        DateTimeZone zone = getZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }

    protected void assemble(Fields fields) {
        if (getBase().getZone() == DateTimeZone.UTC) {
            // Use zero based century and year of century.
            fields.centuryOfEra = new DividedDateTimeField(
                ISOYearOfEraDateTimeField.INSTANCE, DateTimeFieldType.centuryOfEra(), 100);
            fields.yearOfCentury = new RemainderDateTimeField(
                (DividedDateTimeField) fields.centuryOfEra, DateTimeFieldType.yearOfCentury());
            fields.weekyearOfCentury = new RemainderDateTimeField(
                (DividedDateTimeField) fields.centuryOfEra, DateTimeFieldType.weekyearOfCentury());

            fields.centuries = fields.centuryOfEra.getDurationField();
        }
    }

    /**
     * Checks if this chronology instance equals another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.6
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return "ISO".hashCode() * 11 + getZone().hashCode();
    }

    /**
     * Serialize ISOChronology instances using a small stub. This reduces the
     * serialized size, and deserialized instances come from the cache.
     */
    private Object writeReplace() {
        return new Stub(getZone());
    }

    private static final class Stub implements Serializable {
        private static final long serialVersionUID = -6212696554273812441L;

        private transient DateTimeZone iZone;

        Stub(DateTimeZone zone) {
            iZone = zone;
        }

        private Object readResolve() {
            return ExtendedChronology.getInstance(iZone);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(iZone);
        }

        private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException
        {
            iZone = (DateTimeZone)in.readObject();
        }
    }
    
    public static class RangeDurationField extends DurationField {
        private String name = "quarters";
        
        private static final long iUnitMillis = (long)(1000L * 60L * 60L * 24L * 30L * 3L);
        
        
        @Override
        public long add(long instant, int value) {
            long addition = value * iUnitMillis;  // safe
            return FieldUtils.safeAdd(instant, addition);
        }

        @Override
        public long add(long instant, long value) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int compareTo(DurationField durationField) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return (int)Math.abs((subtrahendInstant - minuendInstant) / iUnitMillis);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return (long)Math.abs((subtrahendInstant - minuendInstant) / iUnitMillis);
        }

        @Override
        public long getMillis(int value) {
            return iUnitMillis;
        }

        @Override
        public long getMillis(long value) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public DurationFieldType getType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getUnitMillis() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getValue(long duration) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getValue(long duration, long instant) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getValueAsLong(long duration) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean isPrecise() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isSupported() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
