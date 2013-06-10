package com.barchart.feed.api.exchange;

public enum ExchangeCode {
	
	// Pulled from profiles table, exchange column
	
	// These appear twice in table, only diff is '_' vs '-' in name
	// INDEX_CFE("INDEX_CFE"),
	// INDEX_DOW("INDEX_DOW"),
	// INDEX_SP("INDEX_SP"),

	AFET,
	AMEX,
	AMSTR,
	ASX,
	BATS,
	BMF,
	BRUSS,
	BSE,
	CASH,
	CBOE,
	CBOT,
	CBOTM,
	CFE,
	CJCOM,
	CME,
	CNSX,
	COMEX,
	CROSS,
	CXMI,
	CZCE,
	DCE,
	DDF,
	DGCX,
	DME,
	ECON,
	EEX,
	EUIDX,
	EUREX,
	FOREX,
	FUND,
	GBLX,
	GREEN,
	HKFE,
	ICE,
	ICEFI,
	ICEUS,
	IFUND,
	IMM,
	INDEX,
	INDEX_CFE("INDEX-CFE"),
	INDEX_DJ2("INDEX-DJ2"),
	INDEX_DJ3("INDEX-DJ3"),
	INDEX_DOW("INDEX-DOW"),
	INDEX_NQ("INDEX-NQ"),
	INDEX_NY("INDEX-NY"),
	INDEX_RL("INDEX-RL"),
	INDEX_SP("INDEX-SP"),
	INDEX_TSX("INDEX-TSX"),
	
	IOM,
	ISE,
	KANEX,
	KCBT,
	KFE,
	LCE,
	LIFFE,
	LME,
	LSE,
	MATBA,
	MATIF,
	MCX,
	MDEX,
	MEFF,
	MGEX,
	MNTRL,
	NASDAQ,
	NCDEX,
	NLIF,
	NS_India("NS India"),
	NSE,
	NYBOT,
	NYMEX,
	NYMI,
	NYSE,
	NZD,
	NZX,
	OSAKA,
	OTC_BB("OTC-BB"),
	OTHER_OTC("OTHER OTC"),
	PSI,
	RATE,
	RATES,
	ROFEX,
	RTS,
	SAFEX,
	SECTOR,
	SFE,
	SHFE,
	SICOM,
	SIMEX,
	TAIWA,
	TFEX,
	TGE,
	TIFFE,
	TOCOM,
	TSE,
	TSX,
	TSX_V("TSX-V"),
	WCE,
	YIELD;

	private final String code;
	
	private ExchangeCode() {
		code = this.name();
	}
	
	private ExchangeCode(final String c) {
		code = c;
	}
	
	public String code() {
		return code;
	}
	
	/*
	<exchange id="AMEX" ddfcode="A" delay="15" description="American Stock Exchange" data_frequency="13" codes="AMEX" feewaiver="no" freedelayed="yes"/>
	<exchange id="ASX" ddfcode="v" delay="15" description="Austrailian Stock Exchange" data_frequency="13" codes="ASX" feewaiver="no" freedelayed="yes"/>
	<exchange id="BATS" ddfcode="X" delay="0" description="BATS" data_frequency="13" codes="BATS" feewaiver="yes" freedelayed="yes"/>
	<exchange id="BMF" ddfcode="2" delay="15" description="BM&F Bovespa S.A. - Bolsa De Valores, Mercadorias e Futuros" data_frequency="13" codes="BMF" feewaiver="no" freedelayed="yes"/>
	<exchange id="BROCK" delay="0" description="Brock Report" data_frequency="1" codes="BROCK" feewaiver="no" freedelayed="no"/>
	<exchange id="CBOT" ddfcode="B" delay="10" description="CMEGroup CBOT" data_frequency="13" codes="CBOT" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CBOTM" ddfcode="B" delay="10" description="CMEGroup CBT (Mini)" data_frequency="13" codes="CBOTM" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CFE" ddfcode="R" delay="10" description="CBOE Futures Exchange" data_frequency="13" codes="CFE" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CME" ddfcode="M" delay="10" description="CMEGroup CME" data_frequency="13" codes="CME,IOM,IMM,WEA" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CNSX" ddfcode="4" delay="15" description="CNSX" data_frequency="13" codes="CNSX" feewaiver="no" freedelayed="yes"/>
	<exchange id="COMEX" ddfcode="E" delay="10" description="CNEGroup COMEX" data_frequency="13" codes="COMEX" feewaiver="yes" freedelayed="yes"/>
	<exchange id="CXMI" ddfcode="E" delay="10" description="CMEGroup COMEX (Mini)" data_frequency="13" codes="CXMI" feewaiver="yes" freedelayed="yes"/>
	<exchange id="DDF" ddfcode="_" delay="0" description="Test Exchange" data_frequency="13" codes="DDF,TEST" feewaiver="no" freedelayed="yes"/>
	<exchange id="DME" delay="10" description="Dubai Mercantile Exchange" data_frequency="13" codes="DME" feewaiver="yes" freedelayed="no"/>
	<exchange id="ECON" delay="15" description="Economic Indicators" data_frequency="1" codes="ECON" feewaiver="no" freedelayed="no"/>
	<exchange id="EUREX" ddfcode="w" delay="15" description="EUREX" data_frequency="1" codes="EUREX" feewaiver="no" freedelayed="yes"/>
	<exchange id="FOREX" ddfcode="$" delay="10" description="FOREX" data_frequency="13" codes="FOREX" feewaiver="no" freedelayed="yes"/>
	<exchange id="FUND" ddfcode="F" delay="15" description="Mutual Funds" data_frequency="1" codes="FUND" feewaiver="no" freedelayed="yes"/>
	<exchange id="GBLX" ddfcode="M" delay="10" description="CMEGroup CME (Globex Mini)" data_frequency="13" codes="GBLX" feewaiver="yes" freedelayed="yes"/>
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
	<exchange id="LSE" delay="20" description="London Stock Exchange" data_frequency="1" codes="LSE" feewaiver="no" freedelayed="eod"/>
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
