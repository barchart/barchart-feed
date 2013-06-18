package com.barchart.feed.inst.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.model.meta.Exchange;

public final class Exchanges {

	private static final Map<String, Exchange> names =
			new HashMap<String, Exchange>();
	
	private static final Map<String, Exchange> codes =
			new HashMap<String, Exchange>();
	
	static {
		
		Exchange temp = new ExchangeImpl("AMEX", "A");
		names.put("AMEX", temp);
		codes.put("A", temp);
		
		temp = new ExchangeImpl("ASX", "v");
		names.put("ASX", temp);
		names.put("Fix_Me_V", temp); // LOL
		codes.put("v", temp);
		
		temp = new ExchangeImpl("BATS", "X");
		names.put("BATS", temp);
		names.put("Fix_Me_X", temp); // LOL
		codes.put("X", temp);
		
		temp = new ExchangeImpl("BMF", "2");
		names.put("BMF", temp);
		names.put("Fix_Me_2", temp); // LOL
		codes.put("2", temp);
		
		temp = new ExchangeImpl("CBOT", "B");
		names.put("CBOT", temp);
		names.put("CBOTM", temp);
		names.put("CME_CBOT", temp);
		codes.put("B", temp);
		
		temp = new ExchangeImpl("CFE", "R");
		names.put("CFE", temp);
		names.put("CBOE_Futures", temp);
		codes.put("R", temp);
		
		temp = new ExchangeImpl("CME", "M");
		names.put("CME", temp);
		names.put("CME_Main", temp);
		names.put("IMM", temp);
		names.put("IOM", temp);
		names.put("WEA", temp);
		names.put("GBLX", temp);
		codes.put("M", temp);
		
		temp = new ExchangeImpl("CNSX", "4");
		names.put("CNSX", temp);
		codes.put("4", temp);
		
		temp = new ExchangeImpl("COMEX", "E");
		names.put("COMEX", temp);
		names.put("CXMI", temp);
		names.put("CME_COMEX", temp);
		codes.put("E", temp);
		
		temp = new ExchangeImpl("EUREX", "w");
		names.put("EUREX", temp);
		codes.put("w", temp);
		
		temp = new ExchangeImpl("FOREX", "$");
		names.put("FOREX", temp);
		names.put("Forex", temp);
		codes.put("$", temp);
		
		temp = new ExchangeImpl("FUND", "v");
		names.put("FUND", temp);
		codes.put("F", temp);
		
		temp = new ExchangeImpl("ICE", "L");
		names.put("ICE", temp);
		names.put("ICE_EU", temp);
		codes.put("L", temp);
		
		temp = new ExchangeImpl("INDEX", "I");
		names.put("INDEX", temp);
		names.put("IDX", temp);
		names.put("Index_NO_DOW_NO_SP", temp);
		codes.put("I", temp);
		
		temp = new ExchangeImpl("IDX_CFE", "r");
		names.put("IDX_CFE", temp);
		names.put("INDEX_CFE", temp);
		names.put("INDEX-CFE", temp);
		codes.put("r", temp);
		
		temp = new ExchangeImpl("IDX_DOW", "O");
		names.put("IDX_DOW", temp);
		names.put("INDEX_DOW", temp);
		names.put("INDEX-DOW", temp);
		codes.put("O", temp);
		
		temp = new ExchangeImpl("IDX_NQ", "i");
		names.put("IDX_NQ", temp);
		names.put("INDEX_NQ", temp);
		codes.put("i", temp);
		
		temp = new ExchangeImpl("IDX_NY", "z");
		names.put("IDX_NY", temp);
		names.put("INDEX-NY", temp);
		codes.put("z", temp);
		
		temp = new ExchangeImpl("IDX_RL", "x");
		names.put("IDX_RL", temp);
		names.put("INDEX_RL", temp);
		names.put("INDEX-RL", temp);
		codes.put("x", temp);
		
		temp = new ExchangeImpl("IDX_SP", "P");
		names.put("IDX_SP", temp);
		names.put("INDEX_SP", temp);
		names.put("INDEX-SP", temp);
		codes.put("P", temp);
		
		temp = new ExchangeImpl("INDEX_TSX", "c");
		names.put("INDEX_TSX", temp);
		names.put("INDEX-TSX", temp);
		codes.put("c", temp);
		
		temp = new ExchangeImpl("IFUND", "5");
		names.put("IFUND", temp);
		codes.put("5", temp);
		
		temp = new ExchangeImpl("INDEX-DJ3", "o");
		names.put("INDEX-DJ3", temp);
		codes.put("o", temp);
		
		temp = new ExchangeImpl("INDEX-EN", "k");
		names.put("INDEX-EN", temp);
		names.put("PSI", temp);
		names.put("AMSTR", temp);
		names.put("BRUSS", temp);
		names.put("EUIDX", temp);
		codes.put("k", temp);
		
		temp = new ExchangeImpl("KCBT", "K");
		names.put("KCBT", temp);
		codes.put("K", temp);
		
		temp = new ExchangeImpl("LCE", "e");
		names.put("LCE", temp);
		codes.put("e", temp);
		
		temp = new ExchangeImpl("LIFFE", "t");
		names.put("LIFFE", temp);
		codes.put("t", temp);
		
		temp = new ExchangeImpl("LME", "1");
		names.put("LME", temp);
		codes.put("1", temp);
		
		temp = new ExchangeImpl("MATIF", "h");
		names.put("MATIF", temp);
		codes.put("h", temp);
		
		temp = new ExchangeImpl("MGEX", "G");
		names.put("MGEX", temp);
		codes.put("G", temp);
		
		temp = new ExchangeImpl("NASDAQ", "Q");
		names.put("NASDAQ", temp);
		names.put("NTDS", temp);
		codes.put("Q", temp);
		
		temp = new ExchangeImpl("NLIF", "Y");
		names.put("NLIF", temp);
		names.put("NYSE_Metals", temp);
		codes.put("Y", temp);
		
		temp = new ExchangeImpl("ICEFI", "C");
		names.put("ICEFI", temp);
		names.put("ICEUS", temp);
		names.put("ICE_US", temp);
		names.put("NYBOT", temp);
		codes.put("C", temp);
		
		temp = new ExchangeImpl("NYMEX", "J");
		names.put("NYMEX", temp);
		names.put("CME_NYMEX", temp);
		names.put("NYMI", temp);
		codes.put("J", temp);
		
		temp = new ExchangeImpl("NYSE", "N");
		names.put("NYSE", temp);
		codes.put("N", temp);
		
		temp = new ExchangeImpl("OTCBB", "D");
		names.put("OTCBB", temp);
		names.put("OTC-BB", temp);
		codes.put("D", temp);
		
		temp = new ExchangeImpl("PINKSHEETS", "U");
		names.put("PINKSHEETS", temp);
		names.put("OTHER OTC", temp);
		codes.put("U", temp);
		
		temp = new ExchangeImpl("RATES", "6");
		names.put("RATES", temp);
		codes.put("6", temp);
		
		temp = new ExchangeImpl("TSX", "S");
		names.put("TSX", temp);
		names.put("Fix_Me_S", temp);
		codes.put("S", temp);
		
		temp = new ExchangeImpl("TSXV", "V");
		names.put("TSXV", temp);
		names.put("TSX-V", temp);
		codes.put("V", temp);
		
		temp = new ExchangeImpl("WPG", "W");
		names.put("WPG", temp);
		names.put("WCE", temp);
		names.put("ICECA", temp);
		codes.put("W", temp);
		
	}
	
	private Exchanges() {
		
	}
	
	public static Exchange fromName(final String name) {
		
		if(names.containsKey(name)) {
			return names.get(name);
		} else {
			return Exchange.NULL_EXCHANGE;
		}
		
	}
	
	public static Exchange fromCode(final String code) {
		
		if(codes.containsKey(code)) {
			return codes.get(code);
		} else {
			return Exchange.NULL_EXCHANGE;
		}
		
	}
	
	private static class ExchangeImpl implements Exchange {
		
		private final String name;
		private final String code;
		
		ExchangeImpl(final String name, final String code) {
			this.name = name;
			this.code = code;
		}

		@Override
		public boolean isNull() {
			return this == Exchange.NULL_EXCHANGE;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public String code() {
			return code;
		}
		
		@Override
		public boolean equals(final Object o) {
			
			if(!(o instanceof Exchange)) {
				return false;
			}
			
			Exchange that = (Exchange)o;
			
			return (name.equals(that.name()) && code.equals(that.code()));
			
		}

	}
	
	
	/*
	<exchange id="AMEX" ddfcode="A" delay="15" description="American Stock Exchange" data_frequency="13" codes="AMEX" feewaiver="no" freedelayed="yes"/>
	<exchange id="ASX" ddfcode="v" delay="15" description="Austrailian Stock Exchange" data_frequency="13" codes="ASX" feewaiver="no" freedelayed="yes"/>
	<exchange id="BATS" ddfcode="X" delay="0" description="BATS" data_frequency="13" codes="BATS" feewaiver="yes" freedelayed="yes"/>
	<exchange id="BMF" ddfcode="2" delay="15" description="BM&F Bovespa S.A. - Bolsa De Valores, Mercadorias e Futuros" data_frequency="13" codes="BMF" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="CBOT" ddfcode="B" delay="10" description="CMEGroup CBOT" data_frequency="13" codes="CBOT" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CBOTM" ddfcode="B" delay="10" description="CMEGroup CBT (Mini)" data_frequency="13" codes="CBOTM" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CFE" ddfcode="R" delay="10" description="CBOE Futures Exchange" data_frequency="13" codes="CFE" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CME" ddfcode="M" delay="10" description="CMEGroup CME" data_frequency="13" codes="CME,IOM,IMM,WEA" feewaiver="yes" freedelayed="yes"/>
	<exchange id="GBLX" ddfcode="M" delay="10" description="CMEGroup CME (Globex Mini)" data_frequency="13" codes="GBLX" feewaiver="yes" freedelayed="yes"/>
	
	<exchange id="CNSX" ddfcode="4" delay="15" description="CNSX" data_frequency="13" codes="CNSX" feewaiver="no" freedelayed="yes"/>
	<exchange id="COMEX" ddfcode="E" delay="10" description="CNEGroup COMEX" data_frequency="13" codes="COMEX" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CXMI" ddfcode="E" delay="10" description="CMEGroup COMEX (Mini)" data_frequency="13" codes="CXMI" feewaiver="yes" freedelayed="yes"/>
	
	<exchange id="EUREX" ddfcode="w" delay="15" description="EUREX" data_frequency="1" codes="EUREX" feewaiver="no" freedelayed="yes"/>
	<exchange id="FOREX" ddfcode="$" delay="10" description="FOREX" data_frequency="13" codes="FOREX" feewaiver="no" freedelayed="yes"/>
	<exchange id="FUND" ddfcode="F" delay="15" description="Mutual Funds" data_frequency="1" codes="FUND" feewaiver="no" freedelayed="yes"/>
	
	
	<exchange id="ICE" ddfcode="L" delay="10" description="ICE Europe" data_frequency="13" codes="ICE" feewaiver="no" freedelayed="yes"/>
	<exchange id="IDX" ddfcode="I" delay="10" description="Index" data_frequency="13" codes="IDX,INDEX" feewaiver="no" freedelayed="yes"/>
	<exchange id="IDX_CFE" ddfcode="r" delay="10" description="Index CFE" data_frequency="13" codes="IDX_CFE,INDEX-CFE,INDEX_CFE" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="IDX_DOW" ddfcode="O" delay="10" description="Index DOW" data_frequency="13" codes="IDX_DOW,INDEX-DOW,INDEX_DOW" feewaiver="no" freedelayed="eod"/>
	<exchange id="IDX_NQ" ddfcode="i" delay="15" description="Index NASDAQ" data_frequency="13" codes="IDX_NQ,INDEX-NQ" feewaiver="no" freedelayed="yes"/>
	<exchange id="IDX_NY" ddfcode="z" delay="15" description="Index NYSE" data_frequency="13" codes="INDEX-NY,IDX_NY" feewaiver="no" freedelayed="yes"/>
	<exchange id="IDX_RL" ddfcode="x" delay="10" description="Index Russell" data_frequency="13" codes="IDX_RL,INDEX-RL,INDEX_RL" feewaiver="no" freedelayed="eod"/>
	
	<exchange id="IDX_SP" ddfcode="P" delay="10" description="Index S&P" data_frequency="13" codes="IDX_SP,INDEX-SP,INDEX_SP" feewaiver="no" freedelayed="eod"/>
	<exchange id="IDX_TSX" ddfcode="c" delay="15" description="Index TSX" data_frequency="13" codes="INDEX-TSX,IDX_TSX" feewaiver="no" freedelayed="yes"/>
	<exchange id="IFUND" ddfcode="5" delay="15" description="International Mutual Funds" data_frequency="13" codes="IFUND" feewaiver="no" freedelayed="yes"/>
	<exchange id="INDEX-DJ2" ddfcode="o" delay="15" description="Dow Jones Indexes" data_frequency="1" codes="INDEX-DJ2" feewaiver="no" freedelayed="eod"/>
	
	<exchange id="INDEX-DJ3" ddfcode="p" delay="15" description="DJ Special License Indexes" data_frequency="1" codes="INDEX-DJ3" feewaiver="no" freedelayed="eod"/>
	<exchange id="INDEX-EN" ddfcode="k" delay="15" description="EuroNext Index" data_frequency="13" codes="PSI,AMSTR,BRUSS,EUIDX,INDEX-EN" feewaiver="no" freedelayed="yes"/>
	<exchange id="KCBT" ddfcode="K" delay="10" description="Kansas City Board of Trade" data_frequency="13" codes="KCBT" feewaiver="no" freedelayed="yes"/>
	<exchange id="LCE" ddfcode="e" delay="15" description="EureNext London Commodities" data_frequency="13" codes="LCE" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="LIFFE" ddfcode="t" delay="15" description="EuroNext LIFFE" data_frequency="13" codes="LIFFE" feewaiver="no" freedelayed="yes"/>
	<exchange id="LME" ddfcode="1" delay="1440" description="London Metal Exchange" data_frequency="15" codes="LME" feewaiver="no" freedelayed="eod"/>
	<exchange id="MATIF" ddfcode="h" delay="15" description="EuroNext Paris Commodities" data_frequency="13" codes="MATIF" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="MDEX" delay="15" description="Malaysia Derivatives Exchange" data_frequency="1" codes="MDEX" feewaiver="no" freedelayed="eod"/>
	<exchange id="MGEX" ddfcode="G" delay="10" description="Minneapolis Grain Exchange" data_frequency="13" codes="MGEX" feewaiver="no" freedelayed="yes"/>
	<exchange id="NASDAQ" ddfcode="Q" delay="15" description="Nasdaq" data_frequency="13" codes="NASDAQ,NTDS" feewaiver="no" freedelayed="yes"/>
	<exchange id="NLIF" ddfcode="Y" delay="10" description="NYSELiffe" data_frequency="13" codes="NLIF" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="NYBOT" ddfcode="C" delay="10" description="ICE US" data_frequency="13" codes="ICEFI,ICEUS,NYBOT" feewaiver="no" freedelayed="yes"/>
	<exchange id="NYMEX" ddfcode="J" delay="10" description="CMEGroup NYMEX" data_frequency="13" codes="NYMEX" feewaiver="yes" freedelayed="yes"/>
	<exchange id="NYMI" ddfcode="J" delay="10" description="CMEGroup NYMEX (Mini)" data_frequency="13" codes="NYMI" feewaiver="yes" freedelayed="yes"/>
	<exchange id="NYSE" ddfcode="N" delay="15" description="New York Stock Exchange" data_frequency="13" codes="NYSE" feewaiver="no" freedelayed="yes"/>
	
	<exchange id="OTCBB" ddfcode="D" delay="15" description="OTC/BB" data_frequency="13" codes="OTCBB,OTC-BB" feewaiver="no" freedelayed="yes"/>
	<exchange id="PINKSHEETS" ddfcode="U" delay="15" description="Pinksheets" data_frequency="13" codes="PINKSHEETS,OTHER OTC" feewaiver="no" freedelayed="yes"/>
	<exchange id="RATES" ddfcode="6" delay="15" description="Interest Rates" data_frequency="1" codes="RATES" feewaiver="no" freedelayed="yes"/>
	<exchange id="SIMEX" delay="1440" description="Singapore Exchange" data_frequency="1" codes="SIMEX" feewaiver="no" freedelayed="eod"/>
	
	<exchange id="TSX" ddfcode="S" delay="15" description="Toronto Stock Exchange" data_frequency="13" codes="TSX" feewaiver="no" freedelayed="yes"/>
	<exchange id="TSXV" ddfcode="V" delay="15" description="Toronto Stock Exchange Venture" data_frequency="13" codes="TSXV,TSX-V" feewaiver="no" freedelayed="yes"/>
	<exchange id="WPG" ddfcode="W" delay="10" description="ICE Canada" data_frequency="13" codes="WPG,WCE,ICECA" feewaiver="no" freedelayed="yes"/>
	
	*/
}
