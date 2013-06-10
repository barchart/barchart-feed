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
	
}
