package com.barchart.feed.inst.enums;

public enum TradingMonth {

	NULL("NULL"), JAN("F"), FEB("G"), MAR("H"), APR("J"),
	MAY("K"), JUN("M"), JUL("N"), AUG("Q"), SEP("U"),
	OCT("V"), NOV("X"), DEC("Z");
	
	public final String code;
	
	private TradingMonth(final String code) {
		this.code = code;
	}
	
	public static TradingMonth fromCode(final String code) {
		for(final TradingMonth m : values()) {
			if(m.code.equals(code)) {
				return m;
			}
		}
		return NULL;
	}
	
	public static TradingMonth[] fromCodes(final String code) {
		
		final char[] codes = code.toCharArray();
		
		final TradingMonth[] months = new TradingMonth[codes.length];
		
		int counter = 0;
		for(char c : codes) {
			TradingMonth temp = fromCode(String.valueOf(c));
			if(temp == NULL) {
				throw new RuntimeException("Bad code string: " + code);
			}
			months[counter] = temp;
			counter++;
		}
		return months;
	}
	
	public static String toCodes(final TradingMonth[] codes) {
		
		final StringBuilder sb = new StringBuilder();
		
		for(final TradingMonth m : codes) {
			sb.append(m.code);
		}
		
		return sb.toString();
		
	}
	
	public static TradingMonth[] all() {
		return new TradingMonth[] {JAN, FEB, MAR, APR,	MAY, 
				JUN, JUL, AUG, SEP, OCT, NOV, DEC };
	}
	
}
