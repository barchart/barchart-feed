package com.barchart.feed.inst.enums;

public enum SyntheticInstrumentType {
	
	Foreign_Exchange("FX"),
	Reduced_Tick("RT"),
	Standard("SP"),
	Equities("EQ"),
	Butterfly("BF"),
	Condor("CF"),
	Strip("FS"),
	Inter_Commodity("IS"),
	Crack("C1"),
	Pack("PK"),
	Month_Pack("MP"),
	Pack_Butterfly("PB"),
	Double_Butterfly("DF"),
	Pack_Spread("PS"),
	Bundle("FB"),
	Bundle_Spread("BS"),
	Horizontal_Calendar("HO"),
	Diagonal_Calendar("DO"),
	Straddle("ST"),
	Strangle("SG"),
	Vertical("VT"),
	Equity_Call_Vertical("VC"),
	Equity_Put_Vertical("VP"),
	Box("BX"),
	OptionButterfly("BO"),
	Conditional_Curve("CC"),
	OptionCondor("CO"),
	Double("DB"),
	Horizontal_Straddle("HS"),
	Iron_Condor("IC"),
	Ratio_1x2("12"),
	Ratio_1x3("13"),
	Ratio_2x3("23"),
	OptionStrip("SR"),
	Risk_Reversal("RR"),
	Straddle_Strips("SS"),
	Xmas_Tree("XT"),
	Three_Way("3W"),
	Crack_Spread("CR"),
	Generic("GN");
	
	final String code;
	
	private SyntheticInstrumentType(final String code) {
		this.code = code;
	}
	
	public static SyntheticInstrumentType fromCode(final String code) {
		
		for(SyntheticInstrumentType type: values()) {
			if(type.code.equals(code)) {
				return type;
			}
		}
		return null;
	}

}
