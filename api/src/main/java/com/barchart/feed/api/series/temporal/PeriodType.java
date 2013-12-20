package com.barchart.feed.api.series.temporal;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;


/**
 * Represents periodic intervals of time and provides utility for
 * the specification, analysis and collaboration of typical time intervals
 * associated with financial data.
 * 
 * @author David Ray
 */
public enum PeriodType {
	TICK("Tick", 
		DateTimeComparator.getInstance(
            DateTimeFieldType.millisOfSecond(),
        		DateTimeFieldType.millisOfDay())),
    SECOND("Second", 
        DateTimeComparator.getInstance(
            DateTimeFieldType.secondOfMinute(),
            	DateTimeFieldType.minuteOfHour())),
    MINUTE("Minute", 
        DateTimeComparator.getInstance(
            DateTimeFieldType.minuteOfHour(),
            	DateTimeFieldType.hourOfDay())),
    HOUR("Hour",
        DateTimeComparator.getInstance(
            DateTimeFieldType.hourOfDay(),
            	DateTimeFieldType.dayOfWeek())),
    DAY("Day",
        DateTimeComparator.getInstance(
            DateTimeFieldType.dayOfWeek(),
            	DateTimeFieldType.weekOfWeekyear())),        	
    WEEK("Week",
        DateTimeComparator.getInstance(
            DateTimeFieldType.weekOfWeekyear(),
            	DateTimeFieldType.monthOfYear())),
    MONTH("Month",
        DateTimeComparator.getInstance(
            DateTimeFieldType.monthOfYear(),
            	DateTimeFieldType.year())),
    QUARTER("Quarter", 
        DateTimeComparator.getInstance(
            DateTimeFieldType.monthOfYear(),
            	DateTimeFieldType.year())),
    YEAR("Year",
        DateTimeComparator.getInstance(
            DateTimeFieldType.year()));
	
	/** Display string of this {@code PeriodType} */
	private String type;
    /** Used for operations involving comparisons of time intervals */
    private DateTimeComparator typeComparator;
    
    
    /** 
     * Constructs a new PeriodType
     * 
     * @param type		the display String
     * @param dtc		joda time comparator
     */
    private PeriodType(String type, DateTimeComparator dtc) {
        this.type = type;
        this.typeComparator = dtc;
    }
    
    /**
     * Returns the comparator unique to this type which is used for 
     * comparisons and other utility functions.
     * 
     * @return this type's {@link DateTimeComparator}
     */
    public DateTimeComparator getComparator() {
    	return typeComparator;
    }
    
    /**
     * Convenience method to return a "higher" or "lower" type (num
     * may be negative in which case {@link #dec(PeriodType, int)}
     * will be returned).
     * 
     * @param type		the type to increment
     * @param num		the number of times to increment.
     * @return			the PeriodType specified incremented the amount
     * 					of time specified or the type equal to the maximum or
     * 					minimum type.
     * @see #dec(PeriodType, int)
     */
    public static PeriodType inc(PeriodType type, int num) {
        if(num == 0) return type;
        if(num < 0) {
            return dec(type, num * -1);
        }
        int lev = 0;
        int len = values().length;
        while(++lev <= num && lev <= len) {
            type = higher(type);
        }
        return type;
    }
    
    /**
     * Convenience method to return a "higher" or "lower" type (num
     * may be negative in which case {@link #inc(PeriodType, int)}
     * will be returned).
     * 
     * @param type		the type to decrement
     * @param num		the number of times to decrement.
     * @return			the PeriodType specified decremented the amount
     * 					of time specified or the type equal to the maximum or
     * 					minimum type.
     * @see #inc(PeriodType, int)
     */
    public static PeriodType dec(PeriodType type, int num) {
        if(num == 0) return type;
        if(num < 0) {
            return inc(type, num * -1);
        }
        int lev = num;
        while(--lev > -1) {
            type = lower(type);
        }
        return type;
    }
    
    /**
     * Returns the next lower type unless that type is a week type and 
     * skip weeks is true.
     * 
     * @param type			the type to return the next lower in comparison to.
     * @param skipWeeks		whether or not weeks should be skipped.
     * @return				the next lower type
     * @see #lower(PeriodType)
     */
    public static PeriodType lower(PeriodType type, boolean skipWeeks) {
        PeriodType retVal = lower(type);
        return skipWeeks && retVal == WEEK ? lower(retVal) : retVal;
    }
    
    /**
     * Returns the next lower type
     * 
     * @param type			the type to return the next lower in comparison to.
     * @return				the next lower type
     * @see #lower(PeriodType, boolean)
     * @see #higher(PeriodType)
     */
    public static PeriodType lower(PeriodType type) {
        int index = Math.max(0, type.ordinal() - 1);
        return values()[index];
    }
    
    /**
     * Returns the next higher type unless that type is a week type and 
     * skip weeks is true.
     * 
     * @param type			the type to return the next higher in comparison to.
     * @param skipWeeks		whether or not weeks should be skipped.
     * @return				the next higher type
     * @see #higher(PeriodType)
     */
    public static PeriodType higher(PeriodType type, boolean skipWeeks) {
        PeriodType retVal = higher(type);
        return skipWeeks && retVal == WEEK ? higher(retVal) : retVal;
    }
    
    /**
     * Returns the next higher type
     * 
     * @param type			the type to return the next higher in comparison to.
     * @return				the next higher type
     * @see #higher(PeriodType, boolean)
     * @see #lower(PeriodType)
     */
    public static PeriodType higher(PeriodType type) {
        int index = Math.min(values().length - 1, type.ordinal() + 1);
        return values()[index];
    }
    
    /**
     * Convenience method to return the {@code PeriodType} for the specified
     * string
     * 
     * @param typeString		the name of the type to return.
     * @return					the temporal type matching the specified string.
     */
    public static PeriodType forString(String typeString) {
        for(PeriodType tt : values()) {
            if(tt.type.equalsIgnoreCase(typeString)) {
                return tt;
            }
        }
        return null;
    }
    
    /**
     * Returns a flag indicating whether the specified type is
     * of a greater aggregation than this one.
     * 
     * @param type	the {@code PeriodType} to compare.
     * @return		true if so, false if not.
     */
    public boolean isHigherThan(PeriodType type) {
        return ordinal() > type.ordinal();
    }
    
    /**
     * Returns a flag indicating whether the specified type is
     * of a lower aggregation than this one.
     * 
     * @param type	the {@code PeriodType} to compare.
     * @return		true if so, false if not.
     */
    public boolean isLowerThan(PeriodType type) {
        return type.ordinal() > ordinal();
    }
    
    /**
     * Returns an integer indicating whether the specified {@link DateTime} is
     * equal, less than or greater than this one <em>at the resolution of this {@link PeriodType}</em>.
     * 
     * @param type	the {@code PeriodType} to compare.
     * @return		true if so, false if not.
     */
    public int compareAtResolution(DateTime dt1, DateTime dt2) {
        if(this == PeriodType.QUARTER) {
            dt1 = ExtendedChronology.withPeriodStart(dt1);
            dt2 = ExtendedChronology.withPeriodStart(dt2);
        }
        return typeComparator.compare(dt1, dt2);
    }
    
    /**
     * Returns a flag indicating whether the specified {@link DateTime} is
     * equal to this one <em>at the resolution of this {@link PeriodType}</em>.
     * For example, if this were a {@link PeriodType#QUARTER} type, and a DateTime in 
     * January, were passed in with a DateTime in February, a flag of "true"
     * would be returned because they are equal at the resolution of a 
     * {@link PeriodType#QUARTER}.
     * 
     * @param type	the {@code PeriodType} to compare.
     * @return		true if so, false if not.
     */
    public boolean equalsAtResolution(DateTime dt1, DateTime dt2) {
        if(this == PeriodType.QUARTER) {
            dt1 = ExtendedChronology.withPeriodStart(dt1);
            dt2 = ExtendedChronology.withPeriodStart(dt2);
        }
        return typeComparator.compare(dt1, dt2) == 0;
    }
    
    /**
     * Utility method to return a new DateTime instance with values only in the 
     * relevant field indicated by this {@code PeriodType}. This method is also
     * used for internal hashcode implementations of {@link DatePoint} derivatives.
     * 
     * @param 		dt the source date which will be used to establish base field values.
     * @return		a new DateTime instance whose lesser fields will be aligned to return
     */
    public DateTime resolutionInstant(DateTime dt) {
    	if(this == PeriodType.QUARTER) {
        	return ExtendedChronology.withPeriodStart(dt);
        }
    	
        InstantConverter conv = ConverterManager.getInstance().getInstantConverter(dt);
        //The below DateTime will be queried for a Chronology if it is null.
        Chronology lhsChrono = conv.getChronology(dt, (Chronology)null);
        long lhsMillis = conv.getInstantMillis(dt, lhsChrono);
        lhsMillis = typeComparator.getLowerLimit().getField(lhsChrono).roundFloor(lhsMillis);
        return new DateTime(lhsMillis);
    }
}
