package com.barchart.feed.base.provider;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Symbology {

	private static final Logger log = LoggerFactory.getLogger(Symbology.class);
	
	private Symbology() {
		
	}

	public enum ExpireMonth {

		/*
		 * EXPIRATION MONTH CODE 1 Byte, Alphabetic. It indicates the expiration
		 * month for the security future.
		 */

		JAN('F', 1), //
		JAN_Long('A', 1), //
		FEB('G', 2), //
		FEB_Long('B', 2), //
		MAR('H', 3), //
		MAR_Long('C', 3), //
		APR('J', 4), //
		APR_Long('D', 4), //
		MAY('K', 5), //
		MAY_Long('E', 5), //
		JUN('M', 6), //
		JUN_Long('I', 6), //
		JUL('N', 7), //
		JUL_Long('L', 7), //
		AUG('Q', 8), //
		AUG_Long('O', 8), //
		SEP('U', 9), //
		SEP_Long('P', 9), //
		OCT('V', 10), //
		OCT_Long('R', 10), //
		NOV('X', 11), //
		NOV_Long('S', 11), //
		DEC('Z', 12), //
		DEC_Long('T', 12), //

		CASH('Y', 0), //

		UNKNOWN('?', -1), //

		;

		private static final Logger log = LoggerFactory
				.getLogger(ExpireMonth.class);

		/** The code. */
		public final char code;

		/** The value. */
		public final int value;

		private ExpireMonth(char code, int value) {
			this.code = code;
			this.value = value;
		}

		/**
		 * From code.
		 *
		 * @param code the code
		 * @return the dD f_ expire month
		 */
		public static final ExpireMonth fromCode(final char code) {
			switch (code) {
			case 'F':
				return JAN;
			case 'G':
				return FEB;
			case 'H':
				return MAR;
			case 'J':
				return APR;
			case 'K':
				return MAY;
			case 'M':
				return JUN;
			case 'N':
				return JUL;
			case 'Q':
				return AUG;
			case 'U':
				return SEP;
			case 'V':
				return OCT;
			case 'X':
				return NOV;
			case 'Z':
				return DEC;
				//
			case 'Y':
				return CASH;
				// TODO 'long' months
			default:
				log.error(String.format(
						"unknown expiration month code: %1$c (ascii %1$d)", code));
				return UNKNOWN;
			}
		}

		/**
		 * From millis utc.
		 *
		 * @param millisUTC the millis utc
		 * @return the dD f_ expire month
		 */
		public static final ExpireMonth fromMillisUTC(final long millisUTC) {
			final DateTime dateTime = new DateTime(millisUTC, DateTimeZone.UTC);
			return fromDateTime(dateTime);
		}

		/**
		 * From date time.
		 *
		 * @param dateTime the date time
		 * @return the dD f_ expire month
		 */
		public static final ExpireMonth fromDateTime(final DateTime dateTime) {
			if (dateTime == null) {
				return UNKNOWN;
			}
			// DateTime 'default' calendar is the ISO8601
			// JAN==1 ... DEC==12
			final int month = dateTime.getMonthOfYear();
			switch (month) {
			case 1:
				return JAN;
			case 2:
				return FEB;
			case 3:
				return MAR;
			case 4:
				return APR;
			case 5:
				return MAY;
			case 6:
				return JUN;
			case 7:
				return JUL;
			case 8:
				return AUG;
			case 9:
				return SEP;
			case 10:
				return OCT;
			case 11:
				return NOV;
			case 12:
				return DEC;
			default:
				return UNKNOWN;
			}
		}

		/**
		 * Checks if is valid.
		 *
		 * @param code the code
		 * @return true, if is valid
		 */
		public static final boolean isValid(final char code) {
			final ExpireMonth month = fromCode(code);
			return month != UNKNOWN;
		}

	}
	
	private static final int YEAR;
	private static final char MONTH;
	
	static {
		final DateTime now = new DateTime();
		YEAR = now.year().get();
		MONTH = ExpireMonth.fromDateTime(now).code;
	}
	
	/* ***** ***** Symbol Formatting ***** ***** */
	
	private static final char[] T_Z_O = new char[] {'2', '0', '1'};
	private static final char[] T_Z_T = new char[] {'2', '0', '2'};
	private static final char[] T_Z = new char[] {'2', '0'};
	private static final char[] O = new char[] {'1'};
	
	public static String formatSymbol(String symbol) {
		
		try {
		
			if(symbol == null) {
				return "";
			}
			
			final int len = symbol.length();
			
			if(len < 3) {
				return symbol;
			}
			
			/* Spread */
			if(symbol.charAt(0) == '_') {
				return symbol;
			}
			
			/*  Spot index */
			if(symbol.endsWith("Y0")) {
				return symbol.replace("Y0", "Y2000");
			}
			
			/* Option */
			if(symbol.matches(".+\\d(C|P|D|Q)$")) {
				
				/* Already in correct format */
				if(symbol.contains("|")) {
					return symbol;
				}
				
				int pIndex = len - 2;
				while(Character.isDigit(symbol.charAt(pIndex))) {
					
					if(pIndex <= 2) {
						return symbol + " failed";
					}
					
					pIndex--;
				}
	
				final String price = symbol.substring(pIndex + 1, len-1);
				
				final char mon = symbol.charAt(pIndex);
				ExpireMonth sMon = ExpireMonth.fromCode(mon);
				ExpireMonth nowMon = ExpireMonth.fromCode(MONTH);
				final String y = (sMon.value >= nowMon.value) ? String.valueOf(YEAR) :
					String.valueOf(YEAR+1);
				
				final StringBuilder sb = new StringBuilder();
				
				sb.append(symbol.substring(0, pIndex + 1)); // prefix
				sb.append(y); // year
				sb.append("|"); // pipe
				sb.append(price);
				
				if(symbol.matches(".+(C|D)$")) {
					return sb.append("C").toString();
				} else {
					return sb.append("P").toString();
				}
				
			}
			
			/* e.g. GOOG */
			if(!Character.isDigit(symbol.charAt(len - 1))) {
				return symbol;
			}
			
			/* e.g. ESH3 */
			if(!Character.isDigit(symbol.charAt(len - 2))) {
				
				final StringBuilder sb = new StringBuilder(symbol);
				int last = Character.getNumericValue(symbol.charAt(len - 1));
				if(YEAR % 2010 < last) {
					return sb.insert(len - 1, T_Z_O).toString();
				} else if(YEAR % 2010 > last) {
					return sb.insert(len - 1, T_Z_T).toString();
				} else {
					if(symbol.charAt(len - 2) >= MONTH) {
						return sb.insert(len - 1, T_Z_O).toString();
					} else {
						return sb.insert(len - 1, T_Z_T).toString();
					}
				}
				
			}
			
			/* e.g. ESH13 */
			if(!Character.isDigit(symbol.charAt(len - 3))) {
				return new StringBuilder(symbol).insert(len-2, T_Z).toString();
			}
			
			return symbol;
		
		} catch (RuntimeException e) {
			log.error("Exception {} for symbol {}", e, symbol);
			throw e;
		}
		
	}

	public static String formatHistoricalSymbol(String symbol) {
		
		if(symbol == null) {
			return "";
		}
		
		if(symbol.length() < 3) {
			return symbol;
		}
		
		/* e.g. GOOG */
		if(!Character.isDigit(symbol.charAt(symbol.length() - 1))) {
			return symbol;
		}
		
		/* e.g. ESH3 */
		if(!Character.isDigit(symbol.charAt(symbol.length() - 2))) {
			return new StringBuilder(symbol).insert(symbol.length() - 1, O).toString();
		}
		
		return symbol;
	}
	
}
