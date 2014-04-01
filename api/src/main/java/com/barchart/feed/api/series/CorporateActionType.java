package com.barchart.feed.api.series;

/**
 * Corporate action types. This represents official actions that a corporation
 * may take that are of interest to investors or have an impact on the trading
 * price of the stock.
 */
public enum CorporateActionType {

	ASSIMILATION("AS", "Assimilation"),
	ATTACHMENT("AT", "Attachment"),
	BANKRUPTCY("BK", "Bankruptcy"),
	BONUS_ISSUE("BI", "Bonus Issue"),
	BONUS_RIGHT("BR", "Bonus Right"),
	CALL("CA", "Call"),
	CAPITALIZATION("CP", "Capitalization"),
	DIVIDEND("DV", "Dividend"),
	CASH_DISTRIBUTION("DS", "Distribution"),
	CHANGE("CG", "Change"),
	CONSENT("CN", "Consent"),
	CONVERTIBLE_SECURITY_ISSUE("CS", "Convertible Security Issue"),
	DEFAULT("DF", "Default"),
	MEETING("MT", "Meeting"),
	MERGER("MG", "Merger"),
	REORGANIZATION("RE", "Reorganization"),
	STOCK_SPLIT("SS", "Stock Split"),
	SECURITY_SEPARATION("SP", "Security Separation"),
	SPINOFF("SO", "Spinoff"),
	SUBSCRIPTION_OFFER("SU", "Subscription Offer"),
	RIGHTS_ISSUE("RI", "Rights Issue"),
	WARRANTS_ISSUE("WI", "Warrants Issue"),
	ODD_LOT_OFFER("OL", "Odd Lot Offer"),
	DUTCH_AUCTION("DA", "Dutch Auction"),
	TENDER_OFFER("TO", "Tender Offer"),
	TERMINATION("TR", "Termination");

	private final String code;
	private final String name;

	private CorporateActionType(final String code_, final String name_) {

		assert code_ != null;
		assert name_ != null;

		code = code_;
		name = name_;

	}

	/**
	 * The unique short action code.
	 */
	public String code() {
		return code;
	}

	/**
	 * A readable action name.
	 */
	public String actionName() {
		return name;
	}

	/**
	 * Get a corporate action type from a short code.
	 */
	public static CorporateActionType fromCode(final String code) {

		for (final CorporateActionType type : CorporateActionType.values()) {
			if (type.code().equalsIgnoreCase(code))
				return type;
		}

		throw new IllegalArgumentException("Unknown corporate action type");

	}

}